package com.pvpfps.client.modules.misc;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.BooleanSetting;
import com.pvpfps.client.settings.EnumSetting;
import com.pvpfps.client.settings.IntSetting;
import net.minecraft.client.MinecraftClient;

public class AntiAFKModule extends Module {

    public enum Tipo {
        SALTO,
        AGACHAR,
        GIRO
    }

    private final BooleanSetting ativo = addSetting(new BooleanSetting("ativo", false));
    private final IntSetting intervalo = addSetting(new IntSetting("intervalo", 600, 100, 3600, 1));
    private final EnumSetting<Tipo> tipo = addSetting(new EnumSetting<>("tipo", Tipo.SALTO, Tipo.class));

    private int tickCounter;

    public AntiAFKModule() {
        super("AntiAFK", "Evita ser kickado por inatividade em servidores.", Category.MISC);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!ativo.get()) {
            return;
        }

        if (client.player == null || client.world == null) {
            return;
        }

        if (client.currentScreen != null) {
            return;
        }

        tickCounter++;
        if (tickCounter < intervalo.get()) {
            return;
        }

        tickCounter = 0;

        switch (tipo.get()) {
            case SALTO:
                client.player.jump();
                break;
            case AGACHAR:
                boolean sneak = client.options.sneakKey.isPressed();
                client.options.sneakKey.setPressed(!sneak);
                break;
            case GIRO:
                client.player.setYaw(client.player.getYaw() + 5.0F);
                break;
        }
    }

    @Override
    public void onDisable() {
        tickCounter = 0;
    }
}
