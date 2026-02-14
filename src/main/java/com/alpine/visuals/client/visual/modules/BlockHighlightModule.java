package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class BlockHighlightModule extends VisualModule {
    public static boolean active = false;
    public final Setting glowIntensity;

    public BlockHighlightModule() {
        super("Block Highlight", "Beautiful glowing block selection", Category.RENDER);
        glowIntensity = addSetting("Glow", 5, 1, 10);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
