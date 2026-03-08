package com.pvpfps.client.modules.combat;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import net.minecraft.client.MinecraftClient;

public class ToggleSprintModule extends Module {
    public ToggleSprintModule() {
        super("ToggleSprint", "Mantem sprint ativo continuamente.", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        client.player.setSprinting(true);
        client.options.sprintKey.setPressed(true);
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        client.options.sprintKey.setPressed(false);
    }
}
