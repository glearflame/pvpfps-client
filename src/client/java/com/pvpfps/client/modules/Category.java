package com.pvpfps.client.modules;

public enum Category {
    COMBAT("Combat"),
    RENDER("Render"),
    HUD("HUD"),
    PERFORMANCE("Performance"),
    MISC("Misc");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
