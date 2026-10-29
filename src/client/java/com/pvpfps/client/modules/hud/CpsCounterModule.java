package com.pvpfps.client.modules.hud;

import com.pvpfps.client.utils.InputTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class CpsCounterModule extends HudModule {
    public CpsCounterModule() {
        super("CPSCounter", "Exibe CPS (LMB/RMB) com pico da sessao.");
        setEnabled(true);
    }

    @Override
    protected void renderHud(DrawContext context, MinecraftClient client, int x, int y) {
        int left = InputTracker.getLeftCps();
        int right = InputTracker.getRightCps();
        int peakLeft = InputTracker.getMaxLeftCps();
        int peakRight = InputTracker.getMaxRightCps();
        String text = "CPS: " + left + " | " + right + " (pico: " + peakLeft + " | " + peakRight + ")";
        context.drawTextWithShadow(client.textRenderer, text, x, y, 0xFFFFFFFF);
    }
}
