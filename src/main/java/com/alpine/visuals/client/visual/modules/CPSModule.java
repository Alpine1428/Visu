package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class CPSModule extends VisualModule {
    public static boolean active = false;
    public static int leftCPS = 0;
    public static int rightCPS = 0;
    private final List<Long> leftClicks = new ArrayList<>();
    private final List<Long> rightClicks = new ArrayList<>();
    private boolean wasLeft = false, wasRight = false;

    public CPSModule() { super("CPS Counter", "Clicks per second counter (L/R)", Category.HUD); }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; leftCPS = 0; rightCPS = 0; }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.getWindow() == null) return;
        long now = System.currentTimeMillis();
        boolean left = GLFW.glfwGetMouseButton(client.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        boolean right = GLFW.glfwGetMouseButton(client.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
        if (left && !wasLeft) leftClicks.add(now);
        if (right && !wasRight) rightClicks.add(now);
        wasLeft = left; wasRight = right;
        leftClicks.removeIf(t -> now - t > 1000);
        rightClicks.removeIf(t -> now - t > 1000);
        leftCPS = leftClicks.size();
        rightCPS = rightClicks.size();
    }
}
