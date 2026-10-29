package com.pvpfps.client.modules.combat;

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

public class FastBridgeModule extends Module {

    private final BooleanSetting ativo = addSetting(new BooleanSetting("ativo", false));
    private final BooleanSetting lugarFrente = addSetting(new BooleanSetting("lugarFrente", true));
    private final IntSetting delay = addSetting(new IntSetting("delay", 0, 0, 10, 1));

    private int placeCooldown;

    public FastBridgeModule() {
        super("FastBridge", "Coloca blocos abaixo e na frente automaticamente enquanto anda.", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!ativo.get() || client.player == null || client.world == null || client.interactionManager == null) {
            return;
        }

        if (client.currentScreen != null) {
            return;
        }

        if (placeCooldown > 0) {
            placeCooldown--;
        }

        ClientPlayerEntity player = client.player;

        // Coloca abaixo dos pés
        BlockPos below = player.getBlockPos().down();
        if (client.world.getBlockState(below).isAir()) {
            if (placeCooldown <= 0 && !player.getMainHandStack().isEmpty()) {
                placeBlock(client, below, Direction.UP);
                placeCooldown = delay.get();
            }
        }

        // Coloca na frente (direcao do olhar)
        if (lugarFrente.get() && placeCooldown <= 0) {
            Vec3d lookDir = player.getRotationVec(1.0F).normalize();
            BlockPos front = player.getBlockPos().add(
                    (int) Math.round(lookDir.x),
                    0,
                    (int) Math.round(lookDir.z)
            ).down();
            if (client.world.getBlockState(front).isAir() && !player.getMainHandStack().isEmpty()) {
                placeBlock(client, front, Direction.UP);
                placeCooldown = delay.get();
            }
        }
    }

    private void placeBlock(MinecraftClient client, BlockPos pos, Direction dir) {
        client.interactionManager.interactBlock(
                client.player,
                Hand.MAIN_HAND,
                new BlockHitResult(Vec3d.ofCenter(pos), dir, pos, false)
        );
        client.player.swingHand(Hand.MAIN_HAND);
    }

    @Override
    public void onDisable() {
        placeCooldown = 0;
    }
}
