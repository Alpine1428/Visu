package com.alpine.visuals.client;

import com.alpine.visuals.client.visual.VisualManager;
import com.alpine.visuals.client.gui.VisualMenuScreen;
import com.alpine.visuals.client.render.HudRenderer;
import com.alpine.visuals.client.util.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlpineVisualsClient implements ClientModInitializer {
    public static final String MOD_ID = "alpinevisuals";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static AlpineVisualsClient instance;
    private VisualManager visualManager;
    private HudRenderer hudRenderer;
    private ConfigManager configManager;
    private boolean wasKeyPressed = false;

    @Override
    public void onInitializeClient() {
        instance = this;
        LOGGER.info("[Alpine Visuals] v2.0 Loading...");
        configManager = new ConfigManager();
        visualManager = new VisualManager();
        hudRenderer = new HudRenderer(visualManager);
        configManager.loadModules(visualManager);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            boolean pressed = GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
            if (pressed && !wasKeyPressed) client.setScreen(new VisualMenuScreen());
            wasKeyPressed = pressed;
            visualManager.tick(client);
        });

        HudRenderCallback.EVENT.register((ctx, td) -> hudRenderer.render(ctx, td));
        LOGGER.info("[Alpine Visuals] v2.0 Loaded! RIGHT SHIFT to open.");
    }

    public static AlpineVisualsClient getInstance() { return instance; }
    public VisualManager getVisualManager() { return visualManager; }
    public ConfigManager getConfigManager() { return configManager; }
}
