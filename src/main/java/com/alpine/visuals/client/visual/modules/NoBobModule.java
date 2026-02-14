package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;
import net.minecraft.client.MinecraftClient;

public class NoBobModule extends VisualModule {
    public static boolean active = false;
    public NoBobModule() { super("No Bob", "Remove view bobbing completely", Category.PLAYER); }
    @Override public void onEnable() {
        active = true;
        MinecraftClient.getInstance().options.getBobView().setValue(false);
    }
    @Override public void onDisable() { active = false; }
}
