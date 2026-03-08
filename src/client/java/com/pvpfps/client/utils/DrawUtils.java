package com.pvpfps.client.utils;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public final class DrawUtils {
    private DrawUtils() {
    }

    public static void drawPanel(DrawContext context, int x1, int y1, int x2, int y2, int bgColor, int borderColor) {
        context.fill(x1, y1, x2, y2, bgColor);
        context.fill(x1, y1, x2, y1 + 1, borderColor);
        context.fill(x1, y2 - 1, x2, y2, borderColor);
        context.fill(x1, y1, x1 + 1, y2, borderColor);
        context.fill(x2 - 1, y1, x2, y2, borderColor);
    }

    public static void drawLabel(DrawContext context, TextRenderer renderer, String text, int x, int y, int color) {
        context.drawTextWithShadow(renderer, text, x, y, color);
    }
}
