package com.pvpfps.client.modules.combat;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import net.minecraft.client.MinecraftClient;

public class ToggleSneakModule extends Module {
    public ToggleSneakModule() {
        super("ToggleSneak", "Mantem sneak ativo continuamente.", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        client.options.sneakKey.setPressed(true);
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        client.options.sneakKey.setPressed(false);
    }
}
