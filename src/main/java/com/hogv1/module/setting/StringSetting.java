package com.hogv1.module.setting;

/** Bounded text for local lists, block identifiers, and labels. */
public final class StringSetting extends Setting<String> {
    public static final int MAX_LENGTH = 4096;
    public StringSetting(String id, String name, String defaultValue) {
        super(id, name, defaultValue);
        if (defaultValue.length() > MAX_LENGTH) throw new IllegalArgumentException("Default text is too long");
    }
    @Override protected String normalize(String value) {
        return value.length() <= MAX_LENGTH ? value : defaultValue();
    }
}
