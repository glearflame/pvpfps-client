package com.pvpfps.client.modules.render;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.ColorSetting;
import com.pvpfps.client.settings.IntSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.hit.HitResult;

public class BlockOverlayModule extends Module {
    private final IntSetting size = addSetting(new IntSetting("size", 12, 4, 40, 1));
    private final ColorSetting color = addSetting(new ColorSetting("color", 0x66FFFFFF));

    public BlockOverlayModule() {
        super("BlockOverlay", "Destaca o alvo de bloco no centro da tela.", Category.RENDER);
    }

    @Override
    public void onHudRender(DrawContext context, MinecraftClient client) {
        if (client.crosshairTarget == null || client.crosshairTarget.getType() != HitResult.Type.BLOCK) {
            return;
        }

        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int s = size.get();
        int c = color.get();

        context.fill(centerX - s, centerY - s, centerX + s, centerY - s + 1, c);
        context.fill(centerX - s, centerY + s - 1, centerX + s, centerY + s, c);
        context.fill(centerX - s, centerY - s, centerX - s + 1, centerY + s, c);
        context.fill(centerX + s - 1, centerY - s, centerX + s, centerY + s, c);
    }
}
