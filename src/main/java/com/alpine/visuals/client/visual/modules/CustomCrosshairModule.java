package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class CustomCrosshairModule extends VisualModule {
    public static boolean active = false;
    public final Setting rainbow;
    public final Setting dynamicGap;
    public final Setting dotSize;

    public CustomCrosshairModule() {
        super("Custom Crosshair", "Animated crosshair with dynamic gap & rainbow", Category.RENDER);
        rainbow = addSetting("Rainbow", true);
        dynamicGap = addSetting("Dynamic Gap", true);
        dotSize = addSetting("Dot Size", 1, 0, 3);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
