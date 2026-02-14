package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class HudInfoModule extends VisualModule {
    public static boolean active = false;

    public HudInfoModule() {
        super("HUD Info", "Display FPS, TPS, ping and other info", Category.HUD);
        setEnabled(true);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
