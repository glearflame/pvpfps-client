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
                int max = stack.getMaxDamage();
                int percent = max > 0 ? (int) ((remaining * 100.0) / max) : 0;
                // Cor da durabilidade por porcentagem: verde > 75%, amarelo 35-75%, vermelho < 35%.
                int color = percent > 75 ? 0xFF00FF00 : percent >= 35 ? 0xFFFFFF00 : 0xFFFF0000;
                String text = String.valueOf(remaining);
                context.drawTextWithShadow(client.textRenderer, text, x + offset, y + 18, color);
            }
            offset += 18;
        }
    }
}
