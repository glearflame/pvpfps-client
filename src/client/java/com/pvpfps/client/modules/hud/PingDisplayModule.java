package com.pvpfps.client.modules.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;

public class PingDisplayModule extends HudModule {
    public PingDisplayModule() {
        super("PingDisplay", "Exibe ping atual no servidor.");
        setEnabled(true);
    }

    @Override
    protected void renderHud(DrawContext context, MinecraftClient client, int x, int y) {
        int ping = 0;
        if (client.player != null && client.getNetworkHandler() != null) {
            PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
            if (entry != null) {
                ping = entry.getLatency();
            }
        }

        context.drawTextWithShadow(client.textRenderer, "Ping: " + ping + "ms", x, y, 0xFFFFFFFF);
    }
}
