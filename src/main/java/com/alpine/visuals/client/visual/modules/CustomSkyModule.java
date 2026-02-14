package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class CustomSkyModule extends VisualModule {
    public static boolean active = false;
    public final Setting red, green, blue;

    public CustomSkyModule() {
        super("Custom Sky", "Change sky color to your preference", Category.WORLD);
        red = addSetting("Red", 0.5f, 0.0f, 1.0f);
        green = addSetting("Green", 0.3f, 0.0f, 1.0f);
        blue = addSetting("Blue", 0.8f, 0.0f, 1.0f);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
