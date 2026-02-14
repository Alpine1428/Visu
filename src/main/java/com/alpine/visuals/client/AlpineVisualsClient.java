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
    private boolean wasRShiftPressed = false;

    @Override
    public void onInitializeClient() {
        instance = this;
        LOGGER.info("[Alpine Visuals] Initializing...");

        configManager = new ConfigManager();
        visualManager = new VisualManager();
        hudRenderer = new HudRenderer(visualManager);

        configManager.loadModules(visualManager);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            boolean rShiftPressed = GLFW.glfwGetKey(
                client.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT
            ) == GLFW.GLFW_PRESS;

            if (rShiftPressed && !wasRShiftPressed) {
                client.setScreen(new VisualMenuScreen());
            }
            wasRShiftPressed = rShiftPressed;

            visualManager.tick(client);
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            hudRenderer.render(drawContext, tickDelta);
        });

        LOGGER.info("[Alpine Visuals] Loaded! Press RIGHT SHIFT to open menu.");
    }

    public static AlpineVisualsClient getInstance() {
        return instance;
    }

    public VisualManager getVisualManager() {
        return visualManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
