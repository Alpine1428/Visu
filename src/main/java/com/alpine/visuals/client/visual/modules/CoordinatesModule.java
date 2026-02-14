package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class CoordinatesModule extends VisualModule {
    public static boolean active = false;
    public final Setting showNether;
    public final Setting showChunk;

    public CoordinatesModule() {
        super("Coordinates", "XYZ + nether coords + chunk position", Category.HUD);
        showNether = addSetting("Nether Coords", true);
        showChunk = addSetting("Chunk Coords", true);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
