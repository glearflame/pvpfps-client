package com.pvpfps.client.modules.render;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.utils.OptionUtils;
import net.minecraft.client.MinecraftClient;

public class ReducedParticlesModule extends Module {
    private Object previousParticles;

    public ReducedParticlesModule() {
        super("ReducedParticles", "Diminui particulas para reduzir clutter e aumentar FPS.", Category.RENDER);
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        previousParticles = OptionUtils.getOptionValue(client.options, "getParticles");
        OptionUtils.setEnumByName(client.options, "getParticles", "MINIMAL");
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (previousParticles != null) {
            OptionUtils.setOptionValue(client.options, "getParticles", previousParticles);
            previousParticles = null;
        }
    }
}
