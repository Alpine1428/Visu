package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class ArmorHudModule extends VisualModule {
    public static boolean active = false;

    public ArmorHudModule() {
        super("Armor HUD", "Show equipped armor with durability", Category.HUD);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
