package com.pvpfps.client.modules.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class FpsCounterModule extends HudModule {
    public FpsCounterModule() {
        super("FPSCounter", "Exibe FPS atual.");
        setEnabled(true);
    }

    @Override
    protected void renderHud(DrawContext context, MinecraftClient client, int x, int y) {
        String text = "FPS: " + client.getCurrentFps();
        context.drawTextWithShadow(client.textRenderer, text, x, y, 0xFFFFFFFF);
    }
}
