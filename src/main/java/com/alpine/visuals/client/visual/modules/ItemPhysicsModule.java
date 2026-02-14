package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class ItemPhysicsModule extends VisualModule {
    public static boolean active = false;
    public ItemPhysicsModule() { super("Item Physics", "Items lay flat on ground", Category.RENDER); }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
