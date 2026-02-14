package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class TimeChangerModule extends VisualModule {
    public static boolean active = false;
    public static long clientTime = 6000;
    public final Setting time;

    public TimeChangerModule() {
        super("Time Changer", "Change client-side time of day", Category.WORLD);
        time = addSetting("Time", 6000, 0, 24000);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
    @Override public void onTick(net.minecraft.client.MinecraftClient c) {
        clientTime = (long) time.value;
    }
}
