package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class NoHurtCamModule extends VisualModule {
    public static boolean active = false;
    public NoHurtCamModule() { super("No Hurt Cam", "Remove screen shake when taking damage", Category.PLAYER); }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
