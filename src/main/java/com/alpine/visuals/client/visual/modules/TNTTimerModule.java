package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class TNTTimerModule extends VisualModule {
    public static boolean active = false;
    public TNTTimerModule() { super("TNT Timer", "Show remaining fuse time on TNT", Category.MISC); }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
