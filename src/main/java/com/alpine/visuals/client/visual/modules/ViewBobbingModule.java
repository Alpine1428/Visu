package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class ViewBobbingModule extends VisualModule {
    public static boolean active = false;
    public final Setting intensity;

    public ViewBobbingModule() {
        super("View Bobbing+", "Customizable view bobbing intensity", Category.RENDER);
        intensity = addSetting("Intensity", 0.5f, 0.0f, 2.0f);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
