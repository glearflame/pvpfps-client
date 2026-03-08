package com.pvpfps.client.modules.hud;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.IntSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class HudModule extends Module {
    protected final IntSetting x = addSetting(new IntSetting("x", 8, 0, 4000, 1));
    protected final IntSetting y = addSetting(new IntSetting("y", 8, 0, 4000, 1));

    protected HudModule(String name, String description) {
        super(name, description, Category.HUD);
    }

    public IntSetting getXSetting() {
        return x;
    }

    public IntSetting getYSetting() {
        return y;
    }

    @Override
    public final void onHudRender(DrawContext context, MinecraftClient client) {
        renderHud(context, client, x.get(), y.get());
    }

    protected abstract void renderHud(DrawContext context, MinecraftClient client, int x, int y);
}
