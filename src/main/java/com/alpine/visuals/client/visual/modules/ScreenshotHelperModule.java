package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class ScreenshotHelperModule extends VisualModule {
    public static boolean active = false;

    public ScreenshotHelperModule() {
        super("Screenshot+", "Enhanced screenshot with effects", Category.MISC);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
