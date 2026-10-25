package com.pvpfps.client.modules.combat;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.ColorSetting;
import com.pvpfps.client.settings.EnumSetting;
import com.pvpfps.client.settings.IntSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class CustomCrosshairModule extends Module {
    public enum Estilo {
        PONTO,
        CRUZ,
        CIRCULO,
        TRAÇO
    }

    private final EnumSetting<Estilo> estilo = addSetting(new EnumSetting<>("estilo", Estilo.CRUZ, Estilo.class));
    private final ColorSetting cor = addSetting(new ColorSetting("cor", 0xFFFFFFFF));
    private final IntSetting tamanho = addSetting(new IntSetting("tamanho", 2, 1, 10, 1));

    public CustomCrosshairModule() {
        super("CustomCrosshair", "Desenha uma mira customizavel para PvP.", Category.COMBAT);
    }

    @Override
    public void onHudRender(DrawContext context, MinecraftClient client) {
        if (client.currentScreen != null) {
            return;
        }

        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int color = cor.get();
        int size = tamanho.get();

        switch (estilo.get()) {
            case PONTO -> {
                context.fill(centerX - 1, centerY - 1, centerX + 2, centerY + 2, color);
            }
            case CRUZ -> {
                int half = Math.max(1, size / 2);
                context.fill(centerX - half, centerY - size - 2, centerX + half + 1, centerY - 2, color);
                context.fill(centerX - half, centerY + 2, centerX + half + 1, centerY + size + 2, color);
                context.fill(centerX - size - 2, centerY - half, centerX - 2, centerY + half + 1, color);
                context.fill(centerX + 2, centerY - half, centerX + size + 2, centerY + half + 1, color);
            }
            case CIRCULO -> {
                int radius = Math.max(1, size);
                context.fill(centerX - 1, centerY - radius - 2, centerX + 2, centerY - 2, color);
                context.fill(centerX - 1, centerY + 2, centerX + 2, centerY + radius + 2, color);
                context.fill(centerX - radius - 2, centerY - 1, centerX - 2, centerY + 2, color);
                context.fill(centerX + 2, centerY - 1, centerX + radius + 2, centerY + 2, color);
            }
            case TRAÇO -> {
                int half = Math.max(1, size / 2);
                context.fill(centerX - half, centerY - size - 2, centerX + half + 1, centerY - 2, color);
                context.fill(centerX - half, centerY + 2, centerX + half + 1, centerY + size + 2, color);
            }
        }
    }
}
