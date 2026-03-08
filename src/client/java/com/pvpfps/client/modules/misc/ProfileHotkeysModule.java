package com.pvpfps.client.modules.misc;

import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;

public class ProfileHotkeysModule extends Module {
    public ProfileHotkeysModule() {
        super("ProfileHotkeys", "Ativa atalhos de perfis (F6 salvar, F7 carregar).", Category.MISC);
        setEnabled(true);
    }
}
