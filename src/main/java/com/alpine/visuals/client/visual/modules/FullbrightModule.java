package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class FullbrightModule extends VisualModule {
    public static boolean active = false;
    public FullbrightModule() { super("Fullbright", "Maximum gamma everywhere", Category.RENDER); }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
