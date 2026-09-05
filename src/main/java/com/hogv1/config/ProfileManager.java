package com.hogv1.config;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

public final class ProfileManager {
    private final ConfigManager config;
    public ProfileManager(ConfigManager config) { this.config = config; }
    private Path path(String name) { if (name == null || !name.matches("[a-zA-Z0-9 _-]{1,48}")) throw new IllegalArgumentException("Invalid profile name"); return config.profiles().resolve(name + ".json"); }
    public void save(String name) { config.save(path(name)); }
    public void load(String name) { Path p=path(name); if (Files.isRegularFile(p)) config.load(p); }
    public void duplicate(String from, String to) throws IOException { Files.copy(path(from), path(to)); }
    public void delete(String name) throws IOException { Files.deleteIfExists(path(name)); }
    public List<String> list() {
        try (Stream<Path> paths = Files.list(config.profiles())) { return paths.filter(p -> p.getFileName().toString().endsWith(".json")).map(p -> p.getFileName().toString().replaceFirst("\\.json$", "")).sorted().toList(); }
        catch (IOException e) { return List.of(); }
    }
}
