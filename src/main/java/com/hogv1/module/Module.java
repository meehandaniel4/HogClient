package com.hogv1.module;

import com.hogv1.module.setting.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Base lifecycle. Hooks run on Minecraft's client thread. */
public abstract class Module {
    private final String id;
    private final String name;
    private final String description;
    private final Category category;
    private final List<Setting<?>> settings = new ArrayList<>();
    private final List<Setting<?>> settingsView = Collections.unmodifiableList(settings);
    private boolean enabled;
    private int keybind = -1;

    protected Module(String id, String name, String description, Category category) {
        if (id == null || !id.matches("[a-zA-Z0-9_-]+")) throw new IllegalArgumentException("Invalid module id");
        this.id = id;
        this.name = Objects.requireNonNull(name, "name");
        this.description = Objects.requireNonNull(description, "description");
        this.category = Objects.requireNonNull(category, "category");
    }

    public final String id() { return id; }
    public final String name() { return name; }
    public final String description() { return description; }
    public final Category category() { return category; }
    public final boolean isEnabled() { return enabled; }
    public final int keybind() { return keybind; }
    public final void setKeybind(int keybind) { this.keybind = KeybindSetting.valid(keybind) ? keybind : -1; }
    public final List<Setting<?>> settings() { return settingsView; }

    public final void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) {
            try { onEnable(); }
            catch (RuntimeException exception) {
                this.enabled = false;
                try { onDisable(); } catch (RuntimeException cleanup) { exception.addSuppressed(cleanup); }
                throw exception;
            }
        } else onDisable();
    }

    public final void toggle() { setEnabled(!enabled); }
    public <S extends Setting<?>> S add(S setting) {
        Objects.requireNonNull(setting, "setting");
        if (settings.stream().anyMatch(existing -> existing.id().equals(setting.id())))
            throw new IllegalArgumentException("Duplicate setting: " + setting.id());
        settings.add(setting);
        return setting;
    }

    public final Setting<?> setting(String id) {
        return settings.stream().filter(setting -> setting.id().equals(id)).findFirst().orElse(null);
    }
    public final boolean bool(String id) { return required(id, BooleanSetting.class).get(); }
    public final double number(String id) { return required(id, NumberSetting.class).get(); }
    public final String mode(String id) { return required(id, ModeSetting.class).get(); }
    public final int color(String id) { return required(id, ColorSetting.class).get(); }
    public final String string(String id) { return required(id, StringSetting.class).get(); }
    private <S extends Setting<?>> S required(String id, Class<S> type) {
        Setting<?> setting = setting(id);
        if (!type.isInstance(setting)) throw new IllegalArgumentException("Unknown " + type.getSimpleName() + ": " + id);
        return type.cast(setting);
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
    public void onRender() {}
}
