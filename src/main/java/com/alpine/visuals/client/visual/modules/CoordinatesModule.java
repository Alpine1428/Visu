package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class CoordinatesModule extends VisualModule {
    public static boolean active = false;

    public CoordinatesModule() {
        super("Coordinates", "Always show coordinates on screen", Category.HUD);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
