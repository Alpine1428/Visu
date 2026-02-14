package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class TargetHudModule extends VisualModule {
    public static boolean active = false;
    public final Setting showSkin;
    public final Setting showHealthBar;

    public TargetHudModule() {
        super("Target HUD", "Show info about entity you're looking at", Category.HUD);
        showSkin = addSetting("Show Head", true);
        showHealthBar = addSetting("Health Bar", true);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
