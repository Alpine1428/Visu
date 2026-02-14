package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;
import net.minecraft.client.MinecraftClient;

public class SpeedDisplayModule extends VisualModule {
    public static boolean active = false;
    public static double currentSpeed = 0;
    private double lastX, lastZ;

    public SpeedDisplayModule() {
        super("Speed Display", "Show current movement speed in blocks/s", Category.HUD);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; currentSpeed = 0; }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        double dx = client.player.getX() - lastX;
        double dz = client.player.getZ() - lastZ;
        currentSpeed = Math.sqrt(dx * dx + dz * dz) * 20;
        lastX = client.player.getX();
        lastZ = client.player.getZ();
    }
}
