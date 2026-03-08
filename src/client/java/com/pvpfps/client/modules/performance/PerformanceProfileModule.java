package com.pvpfps.client.modules.performance;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.EnumSetting;
import com.pvpfps.client.utils.OptionUtils;
import net.minecraft.client.MinecraftClient;

import java.util.HashMap;
import java.util.Map;

public class PerformanceProfileModule extends Module {
    public enum Preset {
        MAX_FPS,
        BALANCED,
        QUALITY
    }

    private final EnumSetting<Preset> preset = addSetting(new EnumSetting<>("preset", Preset.MAX_FPS, Preset.class));
    private final Map<String, Object> previousValues = new HashMap<>();
    private Preset lastApplied;

    public PerformanceProfileModule() {
        super("PerformanceProfile", "Aplica preset de FPS para baixo stutter.", Category.PERFORMANCE);
        setEnabled(true);
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.options == null) {
            return;
        }

        applyPreset(client, preset.get());
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client == null || client.options == null) {
            return;
        }

        if (preset.get() != lastApplied) {
            applyPreset(client, preset.get());
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.options != null) {
            previousValues.forEach((getter, value) -> OptionUtils.setOptionValue(client.options, getter, value));
        }
        previousValues.clear();
        lastApplied = null;
    }

    private void applyPreset(MinecraftClient client, Preset selectedPreset) {
        if (client == null || client.options == null) {
            return;
        }

        switch (selectedPreset) {
            case MAX_FPS -> {
                setInt(client, "getBiomeBlendRadius", 0);
                setInt(client, "getSimulationDistance", 6);
                setEnum(client, "getParticles", "MINIMAL");
                setEnum(client, "getCloudRenderMode", "OFF");
                setEnum(client, "getGraphicsMode", "FAST");
                setBoolean(client, "getEntityShadows", false);
            }
            case BALANCED -> {
                setInt(client, "getBiomeBlendRadius", 1);
                setInt(client, "getSimulationDistance", 8);
                setEnum(client, "getParticles", "DECREASED");
                setEnum(client, "getCloudRenderMode", "FAST");
                setEnum(client, "getGraphicsMode", "FAST");
                setBoolean(client, "getEntityShadows", false);
            }
            case QUALITY -> {
                setInt(client, "getBiomeBlendRadius", 3);
                setInt(client, "getSimulationDistance", 10);
                setEnum(client, "getParticles", "ALL");
                setEnum(client, "getCloudRenderMode", "FANCY");
                setEnum(client, "getGraphicsMode", "FANCY");
                setBoolean(client, "getEntityShadows", true);
            }
        }

        lastApplied = selectedPreset;
    }

    private void rememberOption(MinecraftClient client, String getterName) {
        if (client == null || client.options == null) {
            return;
        }

        previousValues.computeIfAbsent(getterName, ignored -> OptionUtils.getOptionValue(client.options, getterName));
    }

    private void setInt(MinecraftClient client, String getterName, int value) {
        rememberOption(client, getterName);
        Object current = OptionUtils.getOptionValue(client.options, getterName);
        if (current == null) {
            return;
        }

        if (current instanceof Integer) {
            OptionUtils.setOptionValue(client.options, getterName, value);
        } else if (current instanceof Double) {
            OptionUtils.setOptionValue(client.options, getterName, (double) value);
        }
    }

    private void setBoolean(MinecraftClient client, String getterName, boolean value) {
        rememberOption(client, getterName);
        OptionUtils.setOptionValue(client.options, getterName, value);
    }

    private void setEnum(MinecraftClient client, String getterName, String enumName) {
        rememberOption(client, getterName);
        OptionUtils.setEnumByName(client.options, getterName, enumName);
    }
}
