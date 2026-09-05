package com.hogv1.module.setting;

/** Packed ARGB, including an independently configurable alpha channel. */
public final class ColorSetting extends Setting<Integer> {
    public ColorSetting(String id, String name, int defaultValue) { super(id, name, defaultValue); }
}
