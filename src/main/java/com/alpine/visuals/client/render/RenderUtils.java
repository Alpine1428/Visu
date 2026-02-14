package com.alpine.visuals.client.render;

import net.minecraft.client.gui.DrawContext;

public class RenderUtils {

    public static void drawRoundedRect(DrawContext ctx, int x, int y, int w, int h, int r, int color) {
        ctx.fill(x + r, y, x + w - r, y + h, color);
        ctx.fill(x, y + r, x + w, y + h - r, color);
        ctx.fill(x, y, x + r, y + r, color);
        ctx.fill(x + w - r, y, x + w, y + r, color);
        ctx.fill(x, y + h - r, x + r, y + h, color);
        ctx.fill(x + w - r, y + h - r, x + w, y + h, color);
    }

    public static void drawOutline(DrawContext ctx, int x, int y, int w, int h, int color) {
        ctx.fill(x, y, x + w, y + 1, color);
        ctx.fill(x, y + h - 1, x + w, y + h, color);
        ctx.fill(x, y, x + 1, y + h, color);
        ctx.fill(x + w - 1, y, x + w, y + h, color);
    }

    public static void drawGlowOutline(DrawContext ctx, int x, int y, int w, int h, int color, int glowSize) {
        for (int i = glowSize; i >= 0; i--) {
            int alpha = (int)(((float)(glowSize - i) / glowSize) * ((color >> 24) & 0xFF));
            int gc = (alpha << 24) | (color & 0x00FFFFFF);
            drawOutline(ctx, x - i, y - i, w + i * 2, h + i * 2, gc);
        }
    }

    public static int interpolateColor(int c1, int c2, float f) {
        int a1 = (c1 >> 24) & 0xFF, r1 = (c1 >> 16) & 0xFF, g1 = (c1 >> 8) & 0xFF, b1 = c1 & 0xFF;
        int a2 = (c2 >> 24) & 0xFF, r2 = (c2 >> 16) & 0xFF, g2 = (c2 >> 8) & 0xFF, b2 = c2 & 0xFF;
        int a = (int)(a1 + (a2 - a1) * f);
        int r = (int)(r1 + (r2 - r1) * f);
        int g = (int)(g1 + (g2 - g1) * f);
        int b = (int)(b1 + (b2 - b1) * f);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int rainbowColor(long offset, float sat, float bri) {
        float hue = ((System.currentTimeMillis() + offset) % 3000) / 3000.0f;
        return java.awt.Color.HSBtoRGB(hue, sat, bri);
    }

    public static int withAlpha(int color, int alpha) {
        alpha = Math.max(0, Math.min(255, alpha));
        return (alpha << 24) | (color & 0x00FFFFFF);
    }
}
