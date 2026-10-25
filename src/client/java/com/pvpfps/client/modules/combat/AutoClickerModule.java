package com.pvpfps.client.modules.combat;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.BooleanSetting;
import com.pvpfps.client.settings.EnumSetting;
import com.pvpfps.client.settings.IntSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

public class AutoClickerModule extends Module {

    public enum ClickMode {
        CLIQUE_ESQUERDO,
        CLIQUE_DIREITO,
        AMBOS
    }

    private final BooleanSetting ativo = addSetting(new BooleanSetting("ativo", false));
    private final EnumSetting<ClickMode> modo = addSetting(new EnumSetting<>("modo", ClickMode.CLIQUE_ESQUERDO, ClickMode.class));
    private final IntSetting cpsEsquerdo = addSetting(new IntSetting("cpsEsquerdo", 10, 1, 30, 1));
    private final IntSetting cpsDireito = addSetting(new IntSetting("cpsDireito", 10, 1, 30, 1));
    private final BooleanSetting aleatorizar = addSetting(new BooleanSetting("aleatorizar", true));

    private int tickCounter;
    private boolean leftKeyState;
    private boolean rightKeyState;

    public AutoClickerModule() {
        super("AutoClicker", "Automatiza cliques de mouse conforme o CPS definido.", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!ativo.get()) {
            leftKeyState = false;
            rightKeyState = false;
            resetKeys(client);
            return;
        }

        if (client.currentScreen != null || client.player == null) {
            return;
        }

        ClickMode m = modo.get();
        boolean leftPressed = m == ClickMode.CLIQUE_ESQUERDO || m == ClickMode.AMBOS;
        boolean rightPressed = m == ClickMode.CLIQUE_DIREITO || m == ClickMode.AMBOS;

        if (leftPressed) {
            int interval = ticksPerClick(cpsEsquerdo.get());
            if (shouldToggle(interval)) {
                leftKeyState = !leftKeyState;
                client.options.attackKey.setPressed(leftKeyState);
            }
        } else if (leftKeyState) {
            leftKeyState = false;
            client.options.attackKey.setPressed(false);
        }

        if (rightPressed) {
            int interval = ticksPerClick(cpsDireito.get());
            if (shouldToggle(interval)) {
                rightKeyState = !rightKeyState;
                client.options.useKey.setPressed(rightKeyState);
            }
        } else if (rightKeyState) {
            rightKeyState = false;
            client.options.useKey.setPressed(false);
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        client.options.attackKey.setPressed(false);
        client.options.useKey.setPressed(false);
        leftKeyState = false;
        rightKeyState = false;
        tickCounter = 0;
    }

    private boolean shouldToggle(int baseInterval) {
        tickCounter++;
        int interval = baseInterval;
        if (aleatorizar.get()) {
            interval += (int) (Math.random() * 5) - 2; // ±2 ticks
            if (interval < 1) {
                interval = 1;
            }
        }
        if (tickCounter >= interval) {
            tickCounter = 0;
            return true;
        }
        return false;
    }

    private int ticksPerClick(int cps) {
        if (cps <= 0) {
            return 20;
        }
        return Math.max(1, 20 / cps);
    }

    private void resetKeys(MinecraftClient client) {
        if (client != null) {
            client.options.attackKey.setPressed(false);
            client.options.useKey.setPressed(false);
        }
    }

    public BooleanSetting getAtivo() {
        return ativo;
    }

    public EnumSetting<ClickMode> getModo() {
        return modo;
    }

    public IntSetting getCpsEsquerdo() {
        return cpsEsquerdo;
    }

    public IntSetting getCpsDireito() {
        return cpsDireito;
    }

    public BooleanSetting getAleatorizar() {
        return aleatorizar;
    }
}
