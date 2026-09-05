package com.hogv1.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Deterministic registry with failure isolation and defensive panic cleanup. */
public final class ModuleManager {
    private static final Logger LOGGER = Logger.getLogger("HogV1/Modules");
    private final Map<String, Module> byId = new LinkedHashMap<>();
    private final List<Module> modules = new ArrayList<>();
    private final List<Module> view = Collections.unmodifiableList(modules);

    public void register(Module module) {
        Objects.requireNonNull(module, "module");
        if (byId.putIfAbsent(module.id(), module) != null) throw new IllegalArgumentException("Duplicate module: " + module.id());
        modules.add(module);
    }
    public List<Module> all() { return view; }
    public Module get(String id) { return byId.get(id); }
    public boolean enabled(String id) { Module module = get(id); return module != null && module.isEnabled(); }
    public void tick() {
        for (Module module : modules) if (module.isEnabled()) {
            try { module.onTick(); }
            catch (RuntimeException exception) { failed(module, exception); }
        }
    }
    public void render() {
        for (Module module : modules) if (module.isEnabled()) {
            try { module.onRender(); }
            catch (RuntimeException exception) { failed(module, exception); }
        }
    }
    public void disableAll() {
        for (Module module : modules) {
            try { module.setEnabled(false); }
            catch (RuntimeException exception) { LOGGER.log(Level.WARNING, "Could not fully clean up " + module.id(), exception); }
        }
    }
    private void failed(Module module, RuntimeException exception) {
        LOGGER.log(Level.WARNING, "Disabled failing module " + module.id(), exception);
        try { module.setEnabled(false); }
        catch (RuntimeException cleanup) { LOGGER.log(Level.WARNING, "Cleanup failed for " + module.id(), cleanup); }
    }
}
