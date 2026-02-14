package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class AnimationsModule extends VisualModule {
    public static boolean active = false;

    public AnimationsModule() {
        super("Animations", "Smooth swing and GUI animations", Category.MISC);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
