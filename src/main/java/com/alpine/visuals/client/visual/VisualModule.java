package com.alpine.visuals.client.visual;

import net.minecraft.client.MinecraftClient;

public abstract class VisualModule {
    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled = false;

    public enum Category {
        RENDER("Render", 0xFF6A5ACD),
        HUD("HUD", 0xFF20B2AA),
        WORLD("World", 0xFFFF6347),
        MISC("Misc", 0xFFDAA520);

        public final String displayName;
        public final int color;

        Category(String displayName, int color) {
            this.displayName = displayName;
            this.color = color;
        }
    }

    public VisualModule(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) onEnable();
            else onDisable();
        }
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick(MinecraftClient client) {}
}
