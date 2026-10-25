package com.pvpfps.client.modules.render;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.DoubleSetting;
import com.pvpfps.client.utils.OptionUtils;
import net.minecraft.client.MinecraftClient;

public class FullbrightModule extends Module {
    private final DoubleSetting gammaValue = addSetting(new DoubleSetting("gammaValue", 16.0D, 1.0D, 100.0D, 1.0D));

    private Object previousGamma;

    public FullbrightModule() {
        super("Fullbright", "Aumenta o brilho global para visibilidade total.", Category.RENDER);
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        try {
            previousGamma = OptionUtils.getOptionValue(client.options, "getGamma");
            double target = gammaValue.get();
            Object targetValue = previousGamma instanceof Integer ? (int) target : target;
            if (!OptionUtils.setOptionValue(client.options, "getGamma", targetValue)) {
                System.err.println("[Fullbright] Falha ao definir gamma. Desativando.");
                setEnabled(false);
            }
        } catch (Exception ex) {
            System.err.println("[Fullbright] Erro ao aplicar gamma: " + ex.getMessage() + ". Desativando.");
            setEnabled(false);
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (previousGamma != null) {
            try {
                OptionUtils.setOptionValue(client.options, "getGamma", previousGamma);
            } catch (Exception ex) {
                System.err.println("[Fullbright] Falha ao restaurar gamma: " + ex.getMessage());
            }
            previousGamma = null;
        }
    }
}
