package com.pvpfps.client.events;

import com.pvpfps.client.modules.ModuleManager;
import com.pvpfps.client.utils.InputTracker;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

import java.util.HashSet;
import java.util.Set;

public class EventManager {
    private final ModuleManager moduleManager;
    private final Set<Integer> pressedKeys = new HashSet<>();

    public EventManager(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    public void register() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> moduleManager.onHudRender(drawContext, MinecraftClient.getInstance()));
    }

    private void onClientTick(MinecraftClient client) {
        InputTracker.tick(client);
        moduleManager.handleKeybinds(client, pressedKeys);
        moduleManager.onTick(client);
    }
}
