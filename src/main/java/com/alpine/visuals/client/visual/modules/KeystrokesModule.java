package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;

public class KeystrokesModule extends VisualModule {
    public static boolean active = false;
    public final Setting showMouse;
    public final Setting showSpace;
    public final Setting rainbowKeys;

    public KeystrokesModule() {
        super("Keystrokes", "Show WASD, mouse buttons, spacebar on screen", Category.HUD);
        showMouse = addSetting("Mouse Buttons", true);
        showSpace = addSetting("Spacebar", true);
        rainbowKeys = addSetting("Rainbow", false);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }
}
