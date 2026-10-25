package com.pvpfps.client.modules.misc;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.BooleanSetting;
import com.pvpfps.client.settings.IntSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ScaffoldModule extends Module {

    private final BooleanSetting ativo = addSetting(new BooleanSetting("ativo", false));
    private final BooleanSetting lugarAbaixo = addSetting(new BooleanSetting("lugarAbaixo", true));
    private final IntSetting delay = addSetting(new IntSetting("delay", 1, 0, 10, 1));

    private int placeCooldown;

    public ScaffoldModule() {
        super("Scaffold", "Coloca blocos automaticamente abaixo dos pés.", Category.MISC);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!ativo.get()) {
            return;
        }

        if (client.currentScreen != null || client.player == null || client.world == null || client.interactionManager == null) {
            return;
        }

        if (placeCooldown > 0) {
            placeCooldown--;
        }

        if (!lugarAbaixo.get()) {
            return;
        }

        ClientPlayerEntity player = client.player;
        BlockPos below = player.getBlockPos().down();

        if (!client.world.getBlockState(below).isAir()) {
            return;
        }

        if (placeCooldown > 0) {
            return;
        }

        ItemStack stack = player.getMainHandStack();
        if (stack.isEmpty()) {
            return;
        }

        placeCooldown = delay.get();

        client.interactionManager.interactBlock(
                player,
                Hand.MAIN_HAND,
                new BlockHitResult(Vec3d.ofCenter(below), Direction.DOWN, below, false)
        );

        player.swingHand(Hand.MAIN_HAND);
    }

    @Override
    public void onDisable() {
        placeCooldown = 0;
    }
}
