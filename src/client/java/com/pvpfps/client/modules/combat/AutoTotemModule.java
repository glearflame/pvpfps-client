package com.pvpfps.client.modules.combat;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.BooleanSetting;
import com.pvpfps.client.settings.IntSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class AutoTotemModule extends Module {

    private final BooleanSetting ativo = addSetting(new BooleanSetting("ativo", false));
    private final IntSetting vidaMinima = addSetting(new IntSetting("vidaMinima", 5, 1, 20, 1));

    public AutoTotemModule() {
        super("AutoTotem", "Move totem de imortalidade para a mão esquerda quando a vida estiver baixa.", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!ativo.get()) {
            return;
        }

        if (client.player == null || client.world == null) {
            return;
        }

        if (client.player.getHealth() >= vidaMinima.get()) {
            return;
        }

        if (!client.player.getInventory().getStack(40).isEmpty()) {
            return;
        }

        int totemSlot = findTotem(client.player.getInventory());
        if (totemSlot < 0) {
            return;
        }

        ItemStack totem = client.player.getInventory().getStack(totemSlot);
        client.player.getInventory().setStack(40, totem.copy());
        client.player.getInventory().setStack(totemSlot, ItemStack.EMPTY);
    }

    private int findTotem(net.minecraft.entity.player.PlayerInventory inventory) {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty() && stack.getItem() == Items.TOTEM_OF_UNDYING) {
                return i;
            }
        }
        return -1;
    }
}
