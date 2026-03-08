package com.pvpfps.client.modules.combat;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.ColorSetting;
import com.pvpfps.client.settings.IntSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class CustomCrosshairModule extends Module {
    private final IntSetting size = addSetting(new IntSetting("size", 6, 2, 20, 1));
    private final IntSetting gap = addSetting(new IntSetting("gap", 3, 0, 12, 1));
    private final IntSetting thickness = addSetting(new IntSetting("thickness", 2, 1, 5, 1));
    private final ColorSetting color = addSetting(new ColorSetting("color", 0xFFFFFFFF));

    public CustomCrosshairModule() {
        super("CustomCrosshair", "Desenha uma mira customizavel para PvP.", Category.COMBAT);
    }

    @Override
    public void onHudRender(DrawContext context, MinecraftClient client) {
        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;

        int halfThickness = Math.max(1, thickness.get() / 2);
        int currentSize = size.get();
        int currentGap = gap.get();
        int c = color.get();

        context.fill(centerX - halfThickness, centerY - currentGap - currentSize, centerX + halfThickness + 1, centerY - currentGap, c);
        context.fill(centerX - halfThickness, centerY + currentGap, centerX + halfThickness + 1, centerY + currentGap + currentSize, c);
        context.fill(centerX - currentGap - currentSize, centerY - halfThickness, centerX - currentGap, centerY + halfThickness + 1, c);
        context.fill(centerX + currentGap, centerY - halfThickness, centerX + currentGap + currentSize, centerY + halfThickness + 1, c);
    }
}
