package com.pvpfps.client.modules.render;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.BooleanSetting;

public class LowFireModule extends Module {
    private final BooleanSetting resourcePackMode = addSetting(new BooleanSetting("resourcePackMode", true));

    public LowFireModule() {
        super("LowFire", "Modo de baixa obstrucao para fogo (ideal com low-fire resource pack).", Category.RENDER);
    }

    public boolean isResourcePackMode() {
        return resourcePackMode.get();
    }
}
