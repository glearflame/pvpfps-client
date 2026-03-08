package com.pvpfps.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.modules.ModuleManager;
import com.pvpfps.client.settings.Setting;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Path rootDir = FabricLoader.getInstance().getConfigDir().resolve("pvpfps-client");
    private final Path profilesDir = rootDir.resolve("profiles");
    private final Path stateFile = rootDir.resolve("state.json");

    private String activeProfile = "default";

    public void init(ModuleManager moduleManager) {
        try {
            Files.createDirectories(profilesDir);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao criar diretorio de configuracao", e);
        }

        loadState();

        if (!Files.exists(profilePath(activeProfile))) {
            saveProfile(activeProfile, moduleManager);
        }

        loadProfile(activeProfile, moduleManager);
    }

    public void saveActiveProfile(ModuleManager moduleManager) {
        saveProfile(activeProfile, moduleManager);
        saveState();
    }

    public void loadActiveProfile(ModuleManager moduleManager) {
        loadProfile(activeProfile, moduleManager);
    }

    public void saveProfile(String profileName, ModuleManager moduleManager) {
        JsonObject root = new JsonObject();
        root.addProperty("profile", profileName);

        JsonObject modulesJson = new JsonObject();
        for (Module module : moduleManager.getModules()) {
            JsonObject moduleJson = new JsonObject();
            moduleJson.addProperty("enabled", module.isEnabled());
            moduleJson.addProperty("keybind", module.getKeybind());

            JsonObject settingsJson = new JsonObject();
            for (Setting<?> setting : module.getSettings()) {
                settingsJson.add(setting.getName(), setting.toJson());
            }

            moduleJson.add("settings", settingsJson);
            modulesJson.add(module.getName(), moduleJson);
        }

        root.add("modules", modulesJson);

        writeJson(profilePath(profileName), root);
    }

    public void loadProfile(String profileName, ModuleManager moduleManager) {
        Path path = profilePath(profileName);
        if (!Files.exists(path)) {
            return;
        }

        JsonObject root = readJson(path);
        if (root == null || !root.has("modules")) {
            return;
        }

        JsonObject modulesJson = root.getAsJsonObject("modules");
        for (Module module : moduleManager.getModules()) {
            if (!modulesJson.has(module.getName())) {
                continue;
            }

            JsonObject moduleJson = modulesJson.getAsJsonObject(module.getName());
            if (moduleJson.has("keybind")) {
                module.setKeybind(moduleJson.get("keybind").getAsInt());
            }

            if (moduleJson.has("settings")) {
                JsonObject settingsJson = moduleJson.getAsJsonObject("settings");
                for (Setting<?> setting : module.getSettings()) {
                    JsonElement jsonValue = settingsJson.get(setting.getName());
                    if (jsonValue != null) {
                        setting.fromJson(jsonValue);
                    }
                }
            }

            if (moduleJson.has("enabled")) {
                module.setEnabled(moduleJson.get("enabled").getAsBoolean());
            }
        }

        activeProfile = profileName;
        saveState();
    }

    public List<String> listProfiles() {
        List<String> profiles = new ArrayList<>();
        try {
            if (!Files.exists(profilesDir)) {
                return profiles;
            }

            Files.list(profilesDir)
                    .filter(path -> path.getFileName().toString().endsWith(".json"))
                    .forEach(path -> {
                        String name = path.getFileName().toString();
                        profiles.add(name.substring(0, name.length() - 5));
                    });
        } catch (IOException ignored) {
        }

        return profiles;
    }

    public String getActiveProfile() {
        return activeProfile;
    }

    public void setActiveProfile(String activeProfile) {
        this.activeProfile = activeProfile;
        saveState();
    }

    private Path profilePath(String profileName) {
        return profilesDir.resolve(profileName + ".json");
    }

    private JsonObject readJson(Path path) {
        try {
            String raw = Files.readString(path, StandardCharsets.UTF_8);
            return JsonParser.parseString(raw).getAsJsonObject();
        } catch (Exception ignored) {
            return null;
        }
    }

    private void writeJson(Path path, JsonObject json) {
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(json), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar configuracao: " + path, e);
        }
    }

    private void loadState() {
        JsonObject state = Optional.ofNullable(readJson(stateFile)).orElseGet(JsonObject::new);
        if (state.has("activeProfile")) {
            activeProfile = state.get("activeProfile").getAsString();
        }
    }

    private void saveState() {
        JsonObject state = new JsonObject();
        state.addProperty("activeProfile", activeProfile);
        writeJson(stateFile, state);
    }
}
