package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class PotionTimerModule extends VisualModule {
    public static boolean active = false;

    public PotionTimerModule() {
        super("Potion Timer", "Show remaining potion effect time", Category.HUD);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
