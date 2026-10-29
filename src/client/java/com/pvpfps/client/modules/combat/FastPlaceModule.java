package com.pvpfps.client.modules.combat;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.BooleanSetting;
import net.minecraft.client.MinecraftClient;

public class FastPlaceModule extends Module {

    private final BooleanSetting ativo = addSetting(new BooleanSetting("ativo", false));

    public FastPlaceModule() {
        super("FastPlace", "Tenta colocar blocos mais rapido (remove delay de colocacao).", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!ativo.get() || client.player == null || client.world == null) {
            return;
        }

        // Nota: O delay de colocacao do Minecraft e controlado internamente pelo
        // ClientPlayerInteractionManager. Sem mixin, nao e possivel desabilitá-lo
        // completamente. Esta estrutura fica preparada para futura integracao
        // com mixin ou evento do Fabric API que exponha esse controle.
    }
}
