package com.pvpfps.client.modules;

import com.pvpfps.client.modules.combat.AutoClickerModule;
import com.pvpfps.client.modules.combat.AutoTotemModule;
import com.pvpfps.client.modules.combat.CustomCrosshairModule;
import com.pvpfps.client.modules.combat.DamageIndicatorModule;
import com.pvpfps.client.modules.combat.HitColorModule;
import com.pvpfps.client.modules.combat.ToggleSneakModule;
import com.pvpfps.client.modules.combat.ToggleSprintModule;
import com.pvpfps.client.modules.combat.ZoomModule;
import com.pvpfps.client.modules.hud.ArmorHudModule;
import com.pvpfps.client.modules.hud.CpsCounterModule;
import com.pvpfps.client.modules.hud.FpsCounterModule;
import com.pvpfps.client.modules.hud.KeystrokesModule;
import com.pvpfps.client.modules.hud.PingDisplayModule;
import com.pvpfps.client.modules.hud.PotionEffectsHudModule;
import com.pvpfps.client.modules.misc.ProfileHotkeysModule;
import com.pvpfps.client.modules.misc.ScaffoldModule;
import com.pvpfps.client.modules.performance.PerformanceProfileModule;
import com.pvpfps.client.modules.render.BlockOverlayModule;
import com.pvpfps.client.modules.render.ClearWaterModule;
import com.pvpfps.client.modules.render.FullbrightModule;
import com.pvpfps.client.modules.render.LowFireModule;
import com.pvpfps.client.modules.render.ReducedParticlesModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    private final Map<Category, List<Module>> byCategory = new EnumMap<>(Category.class);

    public ModuleManager() {
        for (Category category : Category.values()) {
            byCategory.put(category, new ArrayList<>());
        }

        registerDefaults();
        modules.sort(Comparator.comparing(Module::getName));
        byCategory.values().forEach(list -> list.sort(Comparator.comparing(Module::getName)));
    }

    private void registerDefaults() {
        register(new ToggleSprintModule());
        register(new ToggleSneakModule());
        register(new AutoClickerModule());
        register(new AutoTotemModule());
        register(new CustomCrosshairModule());
        register(new HitColorModule());
        register(new DamageIndicatorModule());
        register(new ZoomModule());

        register(new FullbrightModule());
        register(new ClearWaterModule());
        register(new LowFireModule());
        register(new ReducedParticlesModule());
        register(new BlockOverlayModule());

        register(new FpsCounterModule());
        register(new CpsCounterModule());
        register(new PingDisplayModule());
        register(new KeystrokesModule());
        register(new ArmorHudModule());
        register(new PotionEffectsHudModule());

        register(new PerformanceProfileModule());
        register(new ProfileHotkeysModule());
        register(new ScaffoldModule());
    }

    public void register(Module module) {
        modules.add(module);
        byCategory.get(module.getCategory()).add(module);
    }

    public List<Module> getModules() {
        return modules;
    }

    public Collection<Module> getModulesByCategory(Category category) {
        return byCategory.get(category);
    }

    public Optional<Module> getModuleByName(String name) {
        String normalized = name.toLowerCase(Locale.ROOT);
        return modules.stream()
                .filter(m -> m.getName().toLowerCase(Locale.ROOT).equals(normalized))
                .findFirst();
    }

    public <T extends Module> Optional<T> getModuleByClass(Class<T> moduleClass) {
        return modules.stream()
                .filter(moduleClass::isInstance)
                .map(moduleClass::cast)
                .findFirst();
    }

    public void onTick(MinecraftClient client) {
        for (Module module : modules) {
            if (module.isEnabled()) {
                try {
                    module.onTick(client);
                } catch (Throwable t) {
                    System.err.println("[PvPFPSClient] Exceção no onTick do módulo '" + module.getName() + "': " + t);
                    t.printStackTrace();
                }
            }
        }
    }

    public void onHudRender(DrawContext context, MinecraftClient client) {
        for (Module module : modules) {
            if (module.isEnabled()) {
                try {
                    module.onHudRender(context, client);
                } catch (Throwable t) {
                    System.err.println("[PvPFPSClient] Exceção no onHudRender do módulo '" + module.getName() + "': " + t);
                    t.printStackTrace();
                }
            }
        }
    }

    public void handleKeybinds(MinecraftClient client, Set<Integer> pressedKeys) {
        long windowHandle = client.getWindow().getHandle();
        for (Module module : modules) {
            int keybind = module.getKeybind();
            if (keybind < 0) {
                continue;
            }

            boolean currentlyPressed = InputUtil.isKeyPressed(windowHandle, keybind);
            if (currentlyPressed && !pressedKeys.contains(keybind)) {
                if (client.currentScreen == null) {
                    module.toggle();
                }
                pressedKeys.add(keybind);
            } else if (!currentlyPressed) {
                pressedKeys.remove(keybind);
            }
        }
    }
}
