package com.pvpfps.client.modules.hud;

import com.pvpfps.client.utils.InputTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class CpsCounterModule extends HudModule {
    public CpsCounterModule() {
        super("CPSCounter", "Exibe CPS (LMB/RMB).");
        setEnabled(true);
    }

    @Override
    protected void renderHud(DrawContext context, MinecraftClient client, int x, int y) {
        String text = "CPS: " + InputTracker.getLeftCps() + " | " + InputTracker.getRightCps();
        context.drawTextWithShadow(client.textRenderer, text, x, y, 0xFFFFFFFF);
    }
}
