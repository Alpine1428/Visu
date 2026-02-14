package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class HandSwingModule extends VisualModule {
    public static boolean active = false;
    public final Setting swingSpeed;

    public HandSwingModule() {
        super("Swing Anim", "Custom hand swing speed and style", Category.RENDER);
        swingSpeed = addSetting("Speed", 1.0f, 0.1f, 3.0f);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
