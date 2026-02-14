package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class CustomCrosshairModule extends VisualModule {
    public static boolean active = false;

    public CustomCrosshairModule() {
        super("Custom Crosshair", "Beautiful animated crosshair", Category.RENDER);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
