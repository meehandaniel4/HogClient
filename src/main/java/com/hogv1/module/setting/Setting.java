package com.hogv1.module.setting;

import java.util.Objects;

/** A typed, validated value with an immutable default. */
public abstract class Setting<T> {
    private final String id;
    private final String name;
    private final T defaultValue;
    private T value;

    protected Setting(String id, String name, T defaultValue) {
        if (id == null || !id.matches("[a-zA-Z0-9_-]+")) throw new IllegalArgumentException("Invalid setting id");
        this.id = id;
        this.name = Objects.requireNonNull(name, "name");
        this.defaultValue = Objects.requireNonNull(defaultValue, "defaultValue");
        value = defaultValue;
    }

    public final String id() { return id; }
    public final String name() { return name; }
    public final T get() { return value; }
    public final T defaultValue() { return defaultValue; }
    public final void set(T value) { this.value = value == null ? defaultValue : normalize(value); }
    protected T normalize(T value) { return value; }
    public final void reset() { value = defaultValue; }
}
