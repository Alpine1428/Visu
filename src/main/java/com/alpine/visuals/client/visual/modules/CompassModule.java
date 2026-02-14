package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class CompassModule extends VisualModule {
    public static boolean active = false;

    public CompassModule() {
        super("Compass", "Visual compass bar at top of screen", Category.HUD);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
