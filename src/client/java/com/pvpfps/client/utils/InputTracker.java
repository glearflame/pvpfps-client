package com.pvpfps.client.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.ArrayDeque;
import java.util.Deque;

public final class InputTracker {
    private static final Deque<Long> LEFT_CLICKS = new ArrayDeque<>();
    private static final Deque<Long> RIGHT_CLICKS = new ArrayDeque<>();

    private static boolean attackPressedLastTick;
    private static boolean usePressedLastTick;

    private static int trackedEntityId = Integer.MIN_VALUE;
    private static float trackedEntityHealth;
    private static float lastDamage;
    private static long lastDamageTime;
    private static long lastHitTime;

    private InputTracker() {
    }

    public static void tick(MinecraftClient client) {
        long now = System.currentTimeMillis();
        prune(now);

        if (client.player == null || client.world == null) {
            attackPressedLastTick = false;
            usePressedLastTick = false;
            trackedEntityId = Integer.MIN_VALUE;
            return;
        }

        boolean attackPressed = client.options.attackKey.isPressed();
        boolean usePressed = client.options.useKey.isPressed();

        if (attackPressed && !attackPressedLastTick) {
            LEFT_CLICKS.addLast(now);
            captureAttackTarget(client, now);
        }

        if (usePressed && !usePressedLastTick) {
            RIGHT_CLICKS.addLast(now);
        }

        attackPressedLastTick = attackPressed;
        usePressedLastTick = usePressed;

        if (trackedEntityId != Integer.MIN_VALUE) {
            Entity entity = client.world.getEntityById(trackedEntityId);
            if (entity instanceof LivingEntity living) {
                float currentHealth = living.getHealth();
                float delta = trackedEntityHealth - currentHealth;
                if (delta > 0.0F) {
                    lastDamage = delta;
                    lastDamageTime = now;
                    trackedEntityHealth = currentHealth;
                }
            } else {
                trackedEntityId = Integer.MIN_VALUE;
            }
        }
    }

    private static void captureAttackTarget(MinecraftClient client, long now) {
        HitResult hit = client.crosshairTarget;
        if (!(hit instanceof EntityHitResult entityHitResult)) {
            return;
        }

        Entity entity = entityHitResult.getEntity();
        if (!(entity instanceof LivingEntity living)) {
            return;
        }

        trackedEntityId = entity.getId();
        trackedEntityHealth = living.getHealth();
        lastHitTime = now;
    }

    private static void prune(long now) {
        while (!LEFT_CLICKS.isEmpty() && now - LEFT_CLICKS.peekFirst() > 1000L) {
            LEFT_CLICKS.removeFirst();
        }

        while (!RIGHT_CLICKS.isEmpty() && now - RIGHT_CLICKS.peekFirst() > 1000L) {
            RIGHT_CLICKS.removeFirst();
        }
    }

    public static int getLeftCps() {
        return LEFT_CLICKS.size();
    }

    public static int getRightCps() {
        return RIGHT_CLICKS.size();
    }

    public static int getTotalCps() {
        return getLeftCps() + getRightCps();
    }

    public static float getLastDamage() {
        return lastDamage;
    }

    public static boolean hasRecentDamage() {
        return System.currentTimeMillis() - lastDamageTime < 1500L;
    }

    public static boolean hasRecentHit() {
        return System.currentTimeMillis() - lastHitTime < 220L;
    }
}
