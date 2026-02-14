package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class WaypointsModule extends VisualModule {
    public static boolean active = false;
    public final Setting showDistance;
    public final Setting beamHeight;

    public WaypointsModule() {
        super("Waypoints", "Visual waypoint markers (death location)", Category.WORLD);
        showDistance = addSetting("Show Distance", true);
        beamHeight = addSetting("Beam Height", 50, 10, 256);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
