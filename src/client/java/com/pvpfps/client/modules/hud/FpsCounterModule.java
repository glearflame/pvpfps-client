package com.pvpfps.client.modules.hud;

import java.util.ArrayDeque;
import java.util.Deque;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class FpsCounterModule extends HudModule {
    private final Deque<Integer> fpsHistory = new ArrayDeque<>(20);
    private final Deque<Integer> fps100History = new ArrayDeque<>(100);

    public FpsCounterModule() {
        super("FPSCounter", "Exibe FPS atual com media movel e 1% low.");
        setEnabled(true);
    }

    @Override
    protected void renderHud(DrawContext context, MinecraftClient client, int x, int y) {
        int fps = client.getCurrentFps();

        fpsHistory.addLast(fps);
        if (fpsHistory.size() > 20) {
            fpsHistory.removeFirst();
        }

        fps100History.addLast(fps);
        if (fps100History.size() > 100) {
            fps100History.removeFirst();
        }

        double avg = fpsHistory.stream().mapToInt(Integer::intValue).average().orElse((double) fps);
        int low = fps100History.stream().mapToInt(Integer::intValue).min().orElse(fps);

        String text = String.format("FPS: %d (%.0f avg / %d low)", fps, avg, low);
        context.drawTextWithShadow(client.textRenderer, text, x, y, 0xFFFFFFFF);
    }
}
