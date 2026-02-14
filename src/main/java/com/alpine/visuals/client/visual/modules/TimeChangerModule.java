package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class TimeChangerModule extends VisualModule {
    public static boolean active = false;
    public static long clientTime = 6000;

    public TimeChangerModule() {
        super("Time Changer", "Change visual time client-side", Category.WORLD);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
