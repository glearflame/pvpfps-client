package com.pvpfps.client.modules.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class ArmorHudModule extends HudModule {
    public ArmorHudModule() {
        super("ArmorHUD", "Mostra armadura equipada com durabilidade.");
    }

    @Override
    protected void renderHud(DrawContext context, MinecraftClient client, int x, int y) {
        if (client.player == null) {
            return;
        }

        EquipmentSlot[] slots = new EquipmentSlot[] {
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET
        };

        int offset = 0;
        for (EquipmentSlot slot : slots) {
            ItemStack stack = client.player.getEquippedStack(slot);
            context.drawItem(stack, x + offset, y);
            if (!stack.isEmpty() && stack.getMaxDamage() > 0) {
                int remaining = stack.getMaxDamage() - stack.getDamage();
                String text = String.valueOf(remaining);
                context.drawTextWithShadow(client.textRenderer, text, x + offset, y + 18, 0xFFFFFFFF);
            }
            offset += 18;
        }
    }
}
