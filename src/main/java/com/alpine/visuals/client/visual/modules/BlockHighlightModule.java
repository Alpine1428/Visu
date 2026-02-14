package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class BlockHighlightModule extends VisualModule {
    public static boolean active = false;

    public BlockHighlightModule() {
        super("Block Highlight", "Enhanced block selection outline with glow", Category.RENDER);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
