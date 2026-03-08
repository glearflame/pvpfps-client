package com.pvpfps.client.modules.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.Comparator;

public class PotionEffectsHudModule extends HudModule {
    public PotionEffectsHudModule() {
        super("PotionEffectsHUD", "Mostra efeitos ativos de pocao.");
    }

    @Override
    protected void renderHud(DrawContext context, MinecraftClient client, int x, int y) {
        if (client.player == null) {
            return;
        }

        int line = 0;
        for (StatusEffectInstance effect : client.player.getStatusEffects().stream()
                .sorted(Comparator.comparingInt(StatusEffectInstance::getDuration).reversed())
                .toList()) {
            String key = effect.getTranslationKey();
            String label = key.substring(key.lastIndexOf('.') + 1);
            int level = effect.getAmplifier() + 1;
            int seconds = effect.getDuration() / 20;
            String text = label + " " + level + " (" + seconds + "s)";
            context.drawTextWithShadow(client.textRenderer, text, x, y + line * 10, 0xFFB6FFB6);
            line++;
        }
    }
}
