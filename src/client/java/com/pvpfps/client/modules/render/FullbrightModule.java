package com.pvpfps.client.modules.render;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.utils.OptionUtils;
import net.minecraft.client.MinecraftClient;

public class FullbrightModule extends Module {
    private Object previousGamma;

    public FullbrightModule() {
        super("Fullbright", "Aumenta o brilho global para visibilidade total.", Category.RENDER);
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        previousGamma = OptionUtils.getOptionValue(client.options, "getGamma");
        Object target = previousGamma instanceof Integer ? 16 : 16.0D;
        OptionUtils.setOptionValue(client.options, "getGamma", target);
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (previousGamma != null) {
            OptionUtils.setOptionValue(client.options, "getGamma", previousGamma);
            previousGamma = null;
        }
    }
}
