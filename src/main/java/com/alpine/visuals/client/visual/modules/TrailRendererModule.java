package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class TrailRendererModule extends VisualModule {
    public static boolean active = false;
    public final Setting trailLength;

    public TrailRendererModule() {
        super("Trail Renderer", "Smooth movement trail behind player", Category.RENDER);
        trailLength = addSetting("Length", 20, 5, 50);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
