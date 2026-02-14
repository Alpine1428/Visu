package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class AnimationsModule extends VisualModule {
    public static boolean active = false;
    public final Setting oldSwing;
    public final Setting smoothSneak;

    public AnimationsModule() {
        super("Animations", "1.7 style animations and smooth sneaking", Category.MISC);
        oldSwing = addSetting("1.7 Swing", true);
        smoothSneak = addSetting("Smooth Sneak", true);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
