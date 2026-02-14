package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class ArmorHudModule extends VisualModule {
    public static boolean active = false;
    public final Setting showDurability;
    public final Setting showPercentage;

    public ArmorHudModule() {
        super("Armor HUD", "Armor display with durability bars and percentage", Category.HUD);
        showDurability = addSetting("Show Bar", true);
        showPercentage = addSetting("Show %", true);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
