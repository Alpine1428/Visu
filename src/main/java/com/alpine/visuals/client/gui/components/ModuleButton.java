package com.alpine.visuals.client.gui.components;

import com.alpine.visuals.client.render.RenderUtils;
import com.alpine.visuals.client.visual.VisualModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class ModuleButton {
    private final VisualModule module;
    private final int x, baseY, width, height, index;
    private float hoverAnim = 0, enableAnim = 0;

    public ModuleButton(VisualModule m, int x, int y, int w, int h, int idx) {
        module = m; this.x = x; baseY = y; width = w; height = h; index = idx;
    }

    public void render(DrawContext ctx, int mx, int my, float delta, int scrollOff, int gAlpha) {
        MinecraftClient mc = MinecraftClient.getInstance();
        int y = baseY + scrollOff;
        boolean hov = mx >= x && mx < x + width && my >= y && my < y + height;
        boolean en = module.isEnabled();
        long time = System.currentTimeMillis();

        hoverAnim += ((hov ? 1f : 0f) - hoverAnim) * 0.25f;
        enableAnim += ((en ? 1f : 0f) - enableAnim) * 0.15f;

        int catColor = module.getCategory().color;

        // Background
        int bgA = (int)((0.06f + hoverAnim * 0.08f + enableAnim * 0.04f) * gAlpha);
        int bgC = en ? RenderUtils.withAlpha(catColor & 0x00FFFFFF, bgA)
                     : RenderUtils.withAlpha(0xFFFFFF, bgA);
        RenderUtils.drawRoundedRect(ctx, x, y, width, height, 5, bgC);

        // Left accent bar with animation
        int barW = 3;
        int barH = (int)(height * enableAnim * 0.75f);
        int barY = y + (height - barH) / 2;
        if (barH > 0) {
            // Pulsing glow bar
            float pulse = (float)(Math.sin(time / 500.0 + index * 0.5) * 0.3 + 0.7);
            int barA = (int)(enableAnim * gAlpha * pulse);
            int barC = RenderUtils.withAlpha(catColor & 0x00FFFFFF, barA);
            ctx.fill(x + 2, barY, x + 2 + barW, barY + barH, barC);

            // Glow effect
            for (int g = 1; g <= 3; g++) {
                int ga = (int)(barA * 0.2f * (1f - g / 3f));
                ctx.fill(x + 2 - g, barY, x + 2 + barW + g, barY + barH,
                    RenderUtils.withAlpha(catColor & 0x00FFFFFF, ga));
            }
        }

        // Hover highlight line on bottom
        if (hoverAnim > 0.01f) {
            int lineW = (int)(width * hoverAnim * 0.6f);
            int lineX = x + (width - lineW) / 2;
            int lineA = (int)(hoverAnim * 60);
            ctx.fill(lineX, y + height - 1, lineX + lineW, y + height,
                RenderUtils.withAlpha(catColor & 0x00FFFFFF, lineA));
        }

        // Module name
        int nameC = en ? RenderUtils.withAlpha(0xFFFFFF, gAlpha)
                       : RenderUtils.withAlpha(0xBBBBBB, gAlpha);
        ctx.drawText(mc.textRenderer, module.getName(), x + 14, y + 6, nameC, en);

        // Description
        int descC = RenderUtils.withAlpha(0x777777, (int)(gAlpha * 0.7f));
        String desc = module.getDescription();
        if (mc.textRenderer.getWidth(desc) > width - 60) {
            desc = desc.substring(0, Math.min(desc.length(), 35)) + "..";
        }
        ctx.drawText(mc.textRenderer, desc, x + 14, y + 18, descC, false);

        // Settings count indicator
        int settingsCount = module.getSettings().size();
        if (settingsCount > 0) {
            String sc = settingsCount + " opt";
            int scw = mc.textRenderer.getWidth(sc);
            ctx.drawText(mc.textRenderer, sc, x + 14, y + 30,
                RenderUtils.withAlpha(0x555555, (int)(gAlpha * 0.6f)), false);
        }

        // Category color dot
        int dotColor = RenderUtils.withAlpha(catColor & 0x00FFFFFF, (int)(gAlpha * 0.5f));
        ctx.fill(x + 6, y + 7, x + 9, y + 10, dotColor);

        // Toggle switch
        int togX = x + width - 42;
        int togY = y + (height - 16) / 2;
        drawToggle(ctx, togX, togY, en, enableAnim, catColor, gAlpha, time);

        // Separator
        ctx.fill(x + 10, y + height - 1, x + width - 10, y + height,
            RenderUtils.withAlpha(0xFFFFFF, (int)(gAlpha * 0.03f)));
    }

    private void drawToggle(DrawContext ctx, int x, int y, boolean en, float anim, int accent, int alpha, long time) {
        int tw = 32, th = 16;

        // Track with gradient
        int trackC = RenderUtils.interpolateColor(
            RenderUtils.withAlpha(0x222222, alpha),
            RenderUtils.withAlpha(accent & 0x00FFFFFF, (int)(alpha * 0.4f)),
            anim
        );
        RenderUtils.drawRoundedRect(ctx, x, y, tw, th, 8, trackC);

        // Track inner shadow
        ctx.fill(x + 1, y + 1, x + tw - 1, y + 2,
            RenderUtils.withAlpha(0x000000, (int)(alpha * 0.15f)));

        // Knob
        int ks = 12;
        int kx = x + 2 + (int)((tw - ks - 4) * anim);
        int ky = y + 2;

        // Knob shadow
        RenderUtils.drawRoundedRect(ctx, kx + 1, ky + 1, ks, ks, 6,
            RenderUtils.withAlpha(0x000000, (int)(alpha * 0.3f)));

        // Knob body
        int knobC = RenderUtils.interpolateColor(
            RenderUtils.withAlpha(0x777777, alpha),
            RenderUtils.withAlpha(0xFFFFFF, alpha),
            anim
        );
        RenderUtils.drawRoundedRect(ctx, kx, ky, ks, ks, 6, knobC);

        // Knob glow when enabled
        if (anim > 0.5f) {
            float glowPulse = (float)(Math.sin(time / 400.0) * 0.3 + 0.7);
            int ga = (int)((anim - 0.5f) * 2 * 50 * glowPulse);
            RenderUtils.drawRoundedRect(ctx, kx - 2, ky - 2, ks + 4, ks + 4, 8,
                RenderUtils.withAlpha(accent & 0x00FFFFFF, ga));
        }

        // ON/OFF text
        String label = en ? "ON" : "OFF";
        int labelX = en ? x + 4 : x + tw - 16;
        int labelC = RenderUtils.withAlpha(en ? 0xAAFFAA : 0xFF8888, (int)(alpha * 0.5f * anim));
        if (!en) labelC = RenderUtils.withAlpha(0x888888, (int)(alpha * 0.3f));
        // Too small to render text nicely, skip
    }

    public boolean isHovered(int mx, int my, int scrollOff) {
        int y = baseY + scrollOff;
        return mx >= x && mx < x + width && my >= y && my < y + height;
    }

    public VisualModule getModule() { return module; }
    public int getBaseY() { return baseY; }
}
