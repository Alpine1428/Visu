package com.alpine.visuals.client.util;

import com.alpine.visuals.client.AlpineVisualsClient;
import com.alpine.visuals.client.visual.VisualManager;
import com.alpine.visuals.client.visual.VisualModule;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path path;

    public ConfigManager() {
        path = FabricLoader.getInstance().getConfigDir().resolve("alpine_visuals.json");
    }

    public void save(VisualManager vm) {
        try {
            JsonObject root = new JsonObject();
            JsonObject mods = new JsonObject();
            for (VisualModule m : vm.getModules()) {
                JsonObject mod = new JsonObject();
                mod.addProperty("enabled", m.isEnabled());
                for (VisualModule.Setting s : m.getSettings()) {
                    if (s.isToggle) mod.addProperty("s_" + s.name, s.boolValue);
                    else mod.addProperty("s_" + s.name, s.value);
                }
                mods.add(m.getName(), mod);
            }
            root.add("modules", mods);
            try (Writer w = new FileWriter(path.toFile())) { GSON.toJson(root, w); }
        } catch (Exception e) { AlpineVisualsClient.LOGGER.error("Config save failed", e); }
    }

    public void loadModules(VisualManager vm) {
        File f = path.toFile();
        if (!f.exists()) return;
        try (Reader r = new FileReader(f)) {
            JsonObject root = GSON.fromJson(r, JsonObject.class);
            if (root == null || !root.has("modules")) return;
            JsonObject mods = root.getAsJsonObject("modules");
            for (VisualModule m : vm.getModules()) {
                if (!mods.has(m.getName())) continue;
                JsonObject mod = mods.getAsJsonObject(m.getName());
                if (mod.has("enabled")) m.setEnabled(mod.get("enabled").getAsBoolean());
                for (VisualModule.Setting s : m.getSettings()) {
                    String key = "s_" + s.name;
                    if (mod.has(key)) {
                        if (s.isToggle) s.boolValue = mod.get(key).getAsBoolean();
                        else s.value = mod.get(key).getAsFloat();
                    }
                }
            }
        } catch (Exception e) { AlpineVisualsClient.LOGGER.error("Config load failed", e); }
    }
}
