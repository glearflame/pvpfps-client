package com.pvpfps.client.modules.render;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.BooleanSetting;
import com.pvpfps.client.utils.OptionUtils;
import net.minecraft.client.MinecraftClient;

public class ClearWaterModule extends Module {
    private final BooleanSetting disableClouds = addSetting(new BooleanSetting("disableClouds", true));
    private final BooleanSetting disableFog = addSetting(new BooleanSetting("disableFog", false));
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

        if (disableFog.get()) {
            disableWaterFog(client);
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

    /**
     * Tenta desativar a nevoa da agua. O Minecraft nao expõe essa opcao
     * publicamente no GameOptions, entao tentamos por reflexao:
     * client.options ("getWaterFog"). Se o metodo nao existir nesta versao,
     * apenas registra um aviso e segue sem alterar a nevoa (sem mixin, sem
     * quebrar o build).
     */
    private void disableWaterFog(MinecraftClient client) {
        if (client == null || client.options == null) {
            return;
        }

        // Tentativa: opcao getWaterFog em GameOptions (pode nao existir).
        try {
            Object option = OptionUtils.getOptionObject(client.options, "getWaterFog");
            if (option != null) {
                OptionUtils.setOptionValue(client.options, "getWaterFog", false);
            }
        } catch (Exception ignored) {
            // Nao ha suporte para desativar nevoa da agua nesta versao sem mixin.
        }
    }
}
