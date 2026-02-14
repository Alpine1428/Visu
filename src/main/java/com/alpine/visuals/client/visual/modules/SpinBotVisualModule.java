package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;
import net.minecraft.client.MinecraftClient;

public class SpinBotVisualModule extends VisualModule {
    public static boolean active = false;
    public final Setting speed;
    private float angle = 0;

    public SpinBotVisualModule() {
        super("Spin Visual", "Spin your character visually (client-side only)", Category.PLAYER);
        speed = addSetting("Speed", 5, 1, 20);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        angle += speed.value;
        if (angle > 360) angle -= 360;
        client.player.setBodyYaw(angle);
        client.player.setHeadYaw(angle);
    }
}
