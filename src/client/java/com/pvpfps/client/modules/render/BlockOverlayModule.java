package com.pvpfps.client.modules.render;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.ColorSetting;
import com.pvpfps.client.settings.IntSetting;
import com.pvpfps.client.utils.ColorUtils;
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

        // Projetar a posicao do bloco alvo do mundo 3D para coordenadas de tela exigiria
        // a matriz de projecao da camera, que nao e exposta publicamente sem mixin.
        // Mantemos o indicador central, com tamanho configuravel e borda piscante (alpha
        // oscilante) para realcar o alvo.
        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int s = size.get();
        int base = color.get();

        // Alfa oscilante entre ~40% e 100% para criar borda piscante.
        float pulse = (float) (Math.sin(System.currentTimeMillis() / 120.0) * 0.5 + 0.5);
        int alpha = 100 + (int) (155.0 * pulse);
        int c = ColorUtils.withAlpha(base, alpha);

        context.fill(centerX - s, centerY - s, centerX + s, centerY - s + 2, c);
        context.fill(centerX - s, centerY + s - 2, centerX + s, centerY + s, c);
        context.fill(centerX - s, centerY - s, centerX - s + 2, centerY + s, c);
        context.fill(centerX + s - 2, centerY - s, centerX + s, centerY + s, c);
    }
}
