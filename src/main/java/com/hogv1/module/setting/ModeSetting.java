package com.hogv1.module.setting;

import java.util.Arrays;
import java.util.List;

public final class ModeSetting extends Setting<String> {
    private final List<String> options;

    public ModeSetting(String id, String name, String defaultValue, String... options) {
        super(id, name, defaultValue);
        this.options = List.copyOf(Arrays.asList(options));
        if (this.options.isEmpty() || !this.options.contains(defaultValue))
            throw new IllegalArgumentException("Mode default must be an available option");
    }

    @Override protected String normalize(String value) { return options.contains(value) ? value : defaultValue(); }
    public List<String> options() { return options; }
    public void next() { set(options.get((options.indexOf(get()) + 1) % options.size())); }
}
