package com.pvpfps.client.utils;

import java.awt.Color;

public final class ColorUtils {
    private ColorUtils() {
    }

    public static int withAlpha(int color, int alpha) {
        alpha = Math.max(0, Math.min(255, alpha));
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    public static int rgb(int r, int g, int b) {
        return new Color(r, g, b, 255).getRGB();
    }

    public static int argb(int a, int r, int g, int b) {
        return new Color(r, g, b, a).getRGB();
    }
}
