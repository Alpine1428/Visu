package com.alpine.visuals.client.util;

import com.alpine.visuals.client.AlpineVisualsClient;
import com.alpine.visuals.client.visual.VisualManager;
import com.alpine.visuals.client.visual.VisualModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path configPath;

    public ConfigManager() {
        configPath = FabricLoader.getInstance().getConfigDir().resolve("alpine_visuals.json");
    }

    public void save(VisualManager manager) {
        try {
            JsonObject json = new JsonObject();
            JsonObject modules = new JsonObject();
            for (VisualModule m : manager.getModules()) {
                modules.addProperty(m.getName(), m.isEnabled());
            }
            json.add("modules", modules);
            try (Writer w = new FileWriter(configPath.toFile())) {
                GSON.toJson(json, w);
            }
        } catch (IOException e) {
            AlpineVisualsClient.LOGGER.error("Failed to save config", e);
        }
    }

    public void loadModules(VisualManager manager) {
        File file = configPath.toFile();
        if (!file.exists()) return;
        try (Reader r = new FileReader(file)) {
            JsonObject json = GSON.fromJson(r, JsonObject.class);
            if (json == null || !json.has("modules")) return;
            JsonObject modules = json.getAsJsonObject("modules");
            for (VisualModule m : manager.getModules()) {
                if (modules.has(m.getName())) {
                    m.setEnabled(modules.get(m.getName()).getAsBoolean());
                }
            }
        } catch (Exception e) {
            AlpineVisualsClient.LOGGER.error("Failed to load config", e);
        }
    }
}
