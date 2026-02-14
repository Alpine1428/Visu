package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class ZoomModule extends VisualModule {
    public static boolean active = false;
    public static boolean zooming = false;
    public static double zoomFactor = 4.0;
    public final Setting factor;
    public final Setting smoothZoom;

    public ZoomModule() {
        super("Zoom", "Hold C to zoom in with smooth transition", Category.PLAYER);
        factor = addSetting("Factor", 4.0f, 1.5f, 10.0f);
        smoothZoom = addSetting("Smooth", true);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; zooming = false; }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.getWindow() == null) return;
        zooming = GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_C) == GLFW.GLFW_PRESS;
        zoomFactor = factor.value;
    }
}
