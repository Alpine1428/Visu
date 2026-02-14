package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class ChunkBordersModule extends VisualModule {
    public static boolean active = false;

    public ChunkBordersModule() {
        super("Chunk Borders", "Show chunk boundaries", Category.WORLD);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
