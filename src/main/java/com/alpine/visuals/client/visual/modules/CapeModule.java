package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class CapeModule extends VisualModule {
    public static boolean active = false;
    public CapeModule() { super("Cape Anim", "Custom cape animation effects", Category.PLAYER); }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
