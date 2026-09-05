package com.hogv1.module.setting;

public final class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String id, String name, boolean defaultValue) { super(id, name, defaultValue); }
    public void toggle() { set(!get()); }
}
