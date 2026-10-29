package com.pvpfps.client.modules.combat;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.BooleanSetting;
import com.pvpfps.client.settings.DoubleSetting;
import net.minecraft.client.MinecraftClient;

public class ReachModule extends Module {

    private final BooleanSetting ativo = addSetting(new BooleanSetting("ativo", false));
    private final DoubleSetting alcance = addSetting(new DoubleSetting("alcance", 4.5D, 3.0D, 10.0D, 0.5D));

    public ReachModule() {
        super("Reach", "Aumenta o alcance de ataque (funcionalidade limitada sem mixin).", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!ativo.get() || client.player == null || client.world == null) {
            return;
        }

        // Nota: O alcance de ataque e validado tanto pelo cliente quanto pelo servidor.
        // No Minecraft 1.21.8, nao ha API publica do Fabric para alterar o alcance
        // de interacao do jogador sem mixin ou modificacao do pacote de rede.
        // Esta estrutura fica preparada para futura integracao com mixin.
    }
}
