package com.pvpfps.client.modules.combat;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import net.minecraft.client.MinecraftClient;

public class ToggleSprintModule extends Module {
    private boolean isForcing;

    public ToggleSprintModule() {
        super("ToggleSprint", "Mantem sprint ativo continuamente.", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        double velX = client.player.getVelocity().x;
        double velZ = client.player.getVelocity().z;
        boolean moving = Math.abs(velX) > 0.01 || Math.abs(velZ) > 0.01;
        boolean onGround = client.player.isOnGround();

        if (moving) {
            if (onGround && !isForcing) {
                client.player.setSprinting(true);
                client.options.sprintKey.setPressed(true);
                isForcing = true;
            }
        } else if (isForcing) {
            client.options.sprintKey.setPressed(false);
            isForcing = false;
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.options != null && isForcing) {
            client.options.sprintKey.setPressed(false);
            isForcing = false;
        }
    }
}
