package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class FreelookModule extends VisualModule {
    public static boolean active = false;
    public FreelookModule() { super("Freelook", "Look around without changing direction (hold LAlt)", Category.PLAYER); }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
