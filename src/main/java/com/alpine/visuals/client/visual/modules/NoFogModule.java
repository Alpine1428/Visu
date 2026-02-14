package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class NoFogModule extends VisualModule {
    public static boolean active = false;

    public NoFogModule() {
        super("No Fog", "Removes fog for clearer vision", Category.RENDER);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
