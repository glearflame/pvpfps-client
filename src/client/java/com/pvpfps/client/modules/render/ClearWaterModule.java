package com.pvpfps.client.modules.render;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.BooleanSetting;
import com.pvpfps.client.utils.OptionUtils;
import net.minecraft.client.MinecraftClient;

public class ClearWaterModule extends Module {
    private final BooleanSetting disableClouds = addSetting(new BooleanSetting("disableClouds", true));
    private Object previousBiomeBlend;
    private Object previousCloudMode;

    public ClearWaterModule() {
        super("ClearWater", "Aplica ajustes graficos para reduzir poluicao visual em agua.", Category.RENDER);
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        previousBiomeBlend = OptionUtils.getOptionValue(client.options, "getBiomeBlendRadius");
        OptionUtils.setOptionValue(client.options, "getBiomeBlendRadius", 0);

        if (disableClouds.get()) {
            previousCloudMode = OptionUtils.getOptionValue(client.options, "getCloudRenderMode");
            OptionUtils.setEnumByName(client.options, "getCloudRenderMode", "OFF");
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (previousBiomeBlend != null) {
            OptionUtils.setOptionValue(client.options, "getBiomeBlendRadius", previousBiomeBlend);
            previousBiomeBlend = null;
        }

        if (previousCloudMode != null) {
            OptionUtils.setOptionValue(client.options, "getCloudRenderMode", previousCloudMode);
            previousCloudMode = null;
        }
    }
}
