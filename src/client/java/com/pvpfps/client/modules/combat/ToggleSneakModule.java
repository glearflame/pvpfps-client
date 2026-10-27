package com.pvpfps.client.modules.combat;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import net.minecraft.client.MinecraftClient;

public class ToggleSneakModule extends Module {
    private boolean isForcing;

    public ToggleSneakModule() {
        super("ToggleSneak", "Mantem sneak ativo enquanto se move.", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        double velX = client.player.getVelocity().x;
        double velZ = client.player.getVelocity().z;
        boolean moving = Math.abs(velX) > 0.01 || Math.abs(velZ) > 0.01;

        if (moving && !isForcing) {
            client.options.sneakKey.setPressed(true);
            isForcing = true;
        } else if (!moving && isForcing) {
            client.options.sneakKey.setPressed(false);
            isForcing = false;
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.options != null && isForcing) {
            client.options.sneakKey.setPressed(false);
            isForcing = false;
        }
    }
}
