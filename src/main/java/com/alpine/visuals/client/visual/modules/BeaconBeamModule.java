package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class BeaconBeamModule extends VisualModule {
    public static boolean active = false;

    public BeaconBeamModule() {
        super("Beacon Beam", "Enhanced colorful beacon beams", Category.WORLD);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
