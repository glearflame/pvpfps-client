package com.pvpfps.client.modules.combat;

import com.pvpfps.client.PvpFpsClient;
import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.BooleanSetting;
import com.pvpfps.client.settings.ColorSetting;
import com.pvpfps.client.settings.IntSetting;
import com.pvpfps.client.utils.InputTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class DamageIndicatorModule extends Module {
    private final BooleanSetting hitMarker = addSetting(new BooleanSetting("hitMarker", true));
    private final IntSetting yOffset = addSetting(new IntSetting("yOffset", 14, -120, 120, 1));
    private final ColorSetting cor = addSetting(new ColorSetting("cor", 0xFFFF5C5C));

    public DamageIndicatorModule() {
        super("DamageIndicator", "Mostra dano recente e hit marker no centro da tela.", Category.COMBAT);
    }

    @Override
    public void onHudRender(DrawContext context, MinecraftClient client) {
        if (client.currentScreen != null) {
            return;
        }

        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int color = cor.get();

        if (hitMarker.get() && InputTracker.hasRecentHit()) {
            context.drawTextWithShadow(client.textRenderer, "x", centerX + 6, centerY + 6, color);
        }

        if (InputTracker.hasRecentDamage()) {
            String text = String.format("-%.1f", InputTracker.getLastDamage());
            int textWidth = client.textRenderer.getWidth(text);
            context.drawTextWithShadow(client.textRenderer, text, centerX - textWidth / 2, centerY + yOffset.get(), color);
        }
    }
}
