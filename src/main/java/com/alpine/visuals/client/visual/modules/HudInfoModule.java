package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class HudInfoModule extends VisualModule {
    public static boolean active = false;
    public final Setting showFps;
    public final Setting showPing;
    public final Setting showBiome;
    public final Setting showDirection;
    public final Setting showServer;

    public HudInfoModule() {
        super("HUD Info", "FPS, ping, biome, direction, server info", Category.HUD);
        showFps = addSetting("Show FPS", true);
        showPing = addSetting("Show Ping", true);
        showBiome = addSetting("Show Biome", true);
        showDirection = addSetting("Show Direction", true);
        showServer = addSetting("Show Server", true);
        setEnabled(true);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
