package com.pvpfps.client;

import com.pvpfps.client.config.ConfigManager;
import com.pvpfps.client.events.EventManager;
import com.pvpfps.client.gui.ClickGuiScreen;
import com.pvpfps.client.modules.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class PvpFpsClient implements ClientModInitializer {
    private static ModuleManager moduleManager;
    private static ConfigManager configManager;

    public static KeyBinding OPEN_GUI_KEY;
    public static KeyBinding SAVE_PROFILE_KEY;
    public static KeyBinding LOAD_PROFILE_KEY;
    public static KeyBinding ZOOM_KEY;

    @Override
    public void onInitializeClient() {
        moduleManager = new ModuleManager();
        configManager = new ConfigManager();

        registerKeybinds();
        registerEvents();

        configManager.init(moduleManager);
    }

    private void registerKeybinds() {
        OPEN_GUI_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pvpfpsclient.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.pvpfpsclient"
        ));

        SAVE_PROFILE_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pvpfpsclient.save_profile",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F6,
                "category.pvpfpsclient"
        ));

        LOAD_PROFILE_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pvpfpsclient.load_profile",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F7,
                "category.pvpfpsclient"
        ));

        ZOOM_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pvpfpsclient.zoom",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                "category.pvpfpsclient"
        ));
    }

    private void registerEvents() {
        EventManager eventManager = new EventManager(moduleManager);
        eventManager.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_GUI_KEY.wasPressed()) {
                client.setScreen(new ClickGuiScreen());
            }

            if (client.currentScreen == null) {
                while (SAVE_PROFILE_KEY.wasPressed()) {
                    configManager.saveActiveProfile(moduleManager);
                }

                while (LOAD_PROFILE_KEY.wasPressed()) {
                    configManager.loadActiveProfile(moduleManager);
                }
            }
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> configManager.saveActiveProfile(moduleManager));
    }

    public static ModuleManager getModuleManager() {
        return moduleManager;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
