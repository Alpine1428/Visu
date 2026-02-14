package com.alpine.visuals.client.visual;

import net.minecraft.client.MinecraftClient;
import java.util.ArrayList;
import java.util.List;

public abstract class VisualModule {
    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled = false;
    private final List<Setting> settings = new ArrayList<>();

    public enum Category {
        RENDER("Render", 0xFF8B5CF6),
        HUD("HUD", 0xFF06B6D4),
        WORLD("World", 0xFFEF4444),
        PLAYER("Player", 0xFF22C55E),
        MISC("Misc", 0xFFF59E0B);

        public final String displayName;
        public final int color;
        Category(String dn, int c) { displayName = dn; color = c; }
    }

    public static class Setting {
        public final String name;
        public float value, min, max;
        public boolean boolValue;
        public final boolean isToggle;

        public Setting(String name, float value, float min, float max) {
            this.name = name; this.value = value; this.min = min; this.max = max; this.isToggle = false;
        }
        public Setting(String name, boolean value) {
            this.name = name; this.boolValue = value; this.isToggle = true;
            this.value = 0; this.min = 0; this.max = 1;
        }
    }

    public VisualModule(String name, String desc, Category cat) {
        this.name = name; this.description = desc; this.category = cat;
    }

    protected Setting addSetting(String name, float val, float min, float max) {
        Setting s = new Setting(name, val, min, max);
        settings.add(s);
        return s;
    }

    protected Setting addSetting(String name, boolean val) {
        Setting s = new Setting(name, val);
        settings.add(s);
        return s;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public List<Setting> getSettings() { return settings; }

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable(); else onDisable();
    }
    public void setEnabled(boolean e) {
        if (this.enabled != e) { this.enabled = e; if (e) onEnable(); else onDisable(); }
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick(MinecraftClient client) {}
}
