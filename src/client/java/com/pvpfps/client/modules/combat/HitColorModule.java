package com.pvpfps.client.modules.combat;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.ColorSetting;

public class HitColorModule extends Module {
    private final ColorSetting color = addSetting(new ColorSetting("color", 0xFFFF5C5C));

    public HitColorModule() {
        super("HitColor", "Define a cor usada para feedback de hit.", Category.COMBAT);
    }

    public int getColor() {
        return color.get();
    }
}
