package com.pvpfps.client.modules.hud;

import com.pvpfps.client.settings.IntSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class KeystrokesModule extends HudModule {
    private final IntSetting keySize = addSetting(new IntSetting("keySize", 16, 12, 28, 1));

    public KeystrokesModule() {
        super("Keystrokes", "Mostra WASD e botoes do mouse.");
        setEnabled(true);
    }

    @Override
    protected void renderHud(DrawContext context, MinecraftClient client, int x, int y) {
        int size = keySize.get();
        int gap = 2;

        drawKey(context, client, "W", x + size + gap, y, size, client.options.forwardKey.isPressed());
        drawKey(context, client, "A", x, y + size + gap, size, client.options.leftKey.isPressed());
        drawKey(context, client, "S", x + size + gap, y + size + gap, size, client.options.backKey.isPressed());
        drawKey(context, client, "D", x + (size + gap) * 2, y + size + gap, size, client.options.rightKey.isPressed());

        int mouseY = y + (size + gap) * 2 + gap;
        drawKey(context, client, "LMB", x, mouseY, size + gap + size / 2, client.options.attackKey.isPressed());
        drawKey(context, client, "RMB", x + size + gap + size / 2 + gap, mouseY, size + gap + size / 2, client.options.useKey.isPressed());
    }

    private void drawKey(DrawContext context, MinecraftClient client, String label, int x, int y, int size, boolean pressed) {
        int bg = pressed ? 0xCC33CC66 : 0xAA202020;
        int border = pressed ? 0xFF66FF99 : 0xFF555555;

        context.fill(x, y, x + size, y + size, bg);
        context.fill(x, y, x + size, y + 1, border);
        context.fill(x, y + size - 1, x + size, y + size, border);
        context.fill(x, y, x + 1, y + size, border);
        context.fill(x + size - 1, y, x + size, y + size, border);

        int textX = x + (size - client.textRenderer.getWidth(label)) / 2;
        int textY = y + (size - 8) / 2;
        context.drawTextWithShadow(client.textRenderer, label, textX, textY, 0xFFFFFFFF);
    }
}
