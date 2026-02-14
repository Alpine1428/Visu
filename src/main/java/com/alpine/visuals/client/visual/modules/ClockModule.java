package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class ClockModule extends VisualModule {
    public static boolean active = false;
    public final Setting show24h;

    public ClockModule() {
        super("Clock", "Real-time clock display + in-game time", Category.HUD);
        show24h = addSetting("24h Format", true);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
