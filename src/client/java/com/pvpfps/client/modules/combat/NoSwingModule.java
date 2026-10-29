package com.pvpfps.client.modules.combat;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.BooleanSetting;
import net.minecraft.client.MinecraftClient;

public class NoSwingModule extends Module {

    private final BooleanSetting ativo = addSetting(new BooleanSetting("ativo", false));

    public NoSwingModule() {
        super("NoSwing", "Nao anima o braço ao atacar ou colocar blocos.", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!ativo.get() || client.player == null) {
            return;
        }

        // Nota: O swing do braço e acionado internamente pelo Minecraft quando
        // o jogador clica ou coloca um bloco. Sem mixin ou cancelamento de evento
        // do Fabric API, nao eh possivel impedir completamente a animacao.
        // Esta estrutura fica preparada para futura integracao.
    }
}
