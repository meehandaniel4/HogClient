package com.hogv1.module.setting;

/** GLFW keyboard code. -1 means unbound. */
public final class KeybindSetting extends Setting<Integer> {
    public KeybindSetting(String id, String name, int defaultValue) {
        super(id, name, valid(defaultValue) ? defaultValue : -1);
    }
    public static boolean valid(int value) { return value == -1 || value >= 32 && value <= 348; }
    @Override protected Integer normalize(Integer value) { return valid(value) ? value : defaultValue(); }
}
