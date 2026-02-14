package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class ItemModelModule extends VisualModule {
    public static boolean active = false;
    public final Setting scale;
    public final Setting posX, posY;

    public ItemModelModule() {
        super("Item Model", "Customize held item position and scale", Category.MISC);
        scale = addSetting("Scale", 1.0f, 0.5f, 2.0f);
        posX = addSetting("Pos X", 0.0f, -1.0f, 1.0f);
        posY = addSetting("Pos Y", 0.0f, -1.0f, 1.0f);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
