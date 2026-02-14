package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class NameTagsModule extends VisualModule {
    public static boolean active = false;
    public final Setting showHealth;
    public final Setting showDistance;
    public final Setting scale;

    public NameTagsModule() {
        super("Name Tags", "Enhanced nametags with health and distance", Category.RENDER);
        showHealth = addSetting("Show Health", true);
        showDistance = addSetting("Show Distance", true);
        scale = addSetting("Scale", 1.5f, 0.5f, 3.0f);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
