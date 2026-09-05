package com.hogv1.config;

import com.google.gson.*;
import com.hogv1.module.Module;
import com.hogv1.module.ModuleManager;
import com.hogv1.module.setting.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.fabricmc.loader.api.FabricLoader;

/** Resilient JSON persistence. A malformed member is ignored without losing valid peers. */
public final class ConfigManager {
    private static final Logger LOG = Logger.getLogger("HogV1/Config");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final ModuleManager modules;
    private final Path root;
    public ConfigManager(ModuleManager modules) {
        this.modules = modules;
        root = FabricLoader.getInstance().getConfigDir().resolve("hogv1");
    }
    public Path root() { return root; }
    public Path profiles() { return root.resolve("profiles"); }
    public void initialize() {
        try {
            Files.createDirectories(profiles());
            ensure(root.resolve("friends.json"), "[]\n");
            ensure(root.resolve("waypoints.json"), "[]\n");
            Path settings = root.resolve("settings.json");
            if (Files.notExists(settings)) save(); else load(settings);
        } catch (IOException e) { LOG.log(Level.WARNING, "Could not initialize Hog V1 config", e); }
    }
    private static void ensure(Path path, String content) throws IOException {
        if (Files.notExists(path)) Files.writeString(path, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
    }
    public void load(Path path) {
        try {
            JsonObject root = JsonParser.parseString(Files.readString(path)).getAsJsonObject();
            JsonObject map = root.has("modules") && root.get("modules").isJsonObject() ? root.getAsJsonObject("modules") : new JsonObject();
            for (Module module : modules.all()) {
                if (!map.has(module.id()) || !map.get(module.id()).isJsonObject()) continue;
                JsonObject data = map.getAsJsonObject(module.id());
                if (data.has("keybind")) module.setKeybind(data.get("keybind").getAsInt());
                if (data.has("settings") && data.get("settings").isJsonObject()) loadSettings(module, data.getAsJsonObject("settings"));
                if (data.has("enabled")) module.setEnabled(data.get("enabled").getAsBoolean());
            }
        } catch (Exception e) { LOG.log(Level.WARNING, "Ignoring malformed config " + path, e); }
    }
    private void loadSettings(Module module, JsonObject values) {
        for (Setting<?> setting : module.settings()) try {
            if (!values.has(setting.id())) continue;
            JsonElement value = values.get(setting.id());
            if (setting instanceof BooleanSetting x) x.set(value.getAsBoolean());
            else if (setting instanceof NumberSetting x) x.set(value.getAsDouble());
            else if (setting instanceof ColorSetting x) x.set(value.getAsInt());
            else if (setting instanceof KeybindSetting x) x.set(value.getAsInt());
            else if (setting instanceof ModeSetting x) x.set(value.getAsString());
            else if (setting instanceof StringSetting x) x.set(value.getAsString());
        } catch (RuntimeException ignored) { setting.reset(); }
    }
    public void save() { save(root.resolve("settings.json")); }
    public void save(Path path) {
        try {
            Files.createDirectories(path.getParent());
            JsonObject rootObject = new JsonObject(), map = new JsonObject();
            rootObject.addProperty("format", 1); rootObject.add("modules", map);
            for (Module module : modules.all()) {
                JsonObject data = new JsonObject(), settings = new JsonObject();
                data.addProperty("enabled", module.isEnabled()); data.addProperty("keybind", module.keybind());
                for (Setting<?> setting : module.settings()) add(settings, setting.id(), setting.get());
                data.add("settings", settings); map.add(module.id(), data);
            }
            atomicWrite(path, GSON.toJson(rootObject));
        } catch (IOException e) { LOG.log(Level.WARNING, "Could not save config " + path, e); }
    }
    private static void add(JsonObject out, String key, Object value) {
        if (value instanceof Boolean x) out.addProperty(key, x); else if (value instanceof Number x) out.addProperty(key, x); else out.addProperty(key, String.valueOf(value));
    }
    private static void atomicWrite(Path path, String text) throws IOException {
        Path temporary = path.resolveSibling(path.getFileName() + ".tmp");
        Files.writeString(temporary, text, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        try { Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
        catch (AtomicMoveNotSupportedException e) { Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING); }
    }
}
