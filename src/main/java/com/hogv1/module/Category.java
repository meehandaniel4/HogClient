package com.hogv1.module;

/** Click GUI navigation groups. */
public enum Category {
    COMBAT("Combat"), MOVEMENT("Movement"), RENDER("Render"), PLAYER("Player"),
    WORLD("World"), INVENTORY("Inventory"), UTILITY("Utility"), HUD("HUD"), SETTINGS("Settings");

    private final String displayName;
    Category(String displayName) { this.displayName = displayName; }
    public String displayName() { return displayName; }
    @Override public String toString() { return displayName; }
}
