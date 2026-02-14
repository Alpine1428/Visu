package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class ScreenEffectsModule extends VisualModule {
    public static boolean active = false;
    public final Setting vignette;
    public final Setting chromatic;
    public final Setting scanlines;

    public ScreenEffectsModule() {
        super("Screen FX", "Vignette, chromatic aberration, scanlines", Category.MISC);
        vignette = addSetting("Vignette", true);
        chromatic = addSetting("Chromatic", false);
        scanlines = addSetting("Scanlines", false);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
