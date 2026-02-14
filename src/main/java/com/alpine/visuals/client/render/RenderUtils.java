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

    public static void drawSmoothRect(DrawContext ctx, int x, int y, int w, int h, int color) {
        ctx.fill(x, y, x + w, y + h, color);
    }

    public static void drawOutline(DrawContext ctx, int x, int y, int w, int h, int color) {
        ctx.fill(x, y, x + w, y + 1, color);
        ctx.fill(x, y + h - 1, x + w, y + h, color);
        ctx.fill(x, y, x + 1, y + h, color);
        ctx.fill(x + w - 1, y, x + w, y + h, color);
    }

    public static void drawGlowOutline(DrawContext ctx, int x, int y, int w, int h, int color, int size) {
        for (int i = size; i >= 0; i--) {
            int a = (int)(((float)(size - i) / size) * ((color >> 24) & 0xFF));
            int c = (a << 24) | (color & 0x00FFFFFF);
            drawOutline(ctx, x - i, y - i, w + i * 2, h + i * 2, c);
        }
    }

    public static void drawGlowRect(DrawContext ctx, int x, int y, int w, int h, int color, int glowSize) {
        drawRoundedRect(ctx, x, y, w, h, 3, color);
        for (int i = 1; i <= glowSize; i++) {
            int a = (int)(((float)(glowSize - i) / glowSize) * ((color >> 24) & 0xFF) * 0.3f);
            int c = (a << 24) | (color & 0x00FFFFFF);
            drawOutline(ctx, x - i, y - i, w + i * 2, h + i * 2, c);
        }
    }

    public static void drawProgressBar(DrawContext ctx, int x, int y, int w, int h, float progress, int bgColor, int fgColor) {
        drawRoundedRect(ctx, x, y, w, h, 2, bgColor);
        int barW = (int)(w * Math.max(0, Math.min(1, progress)));
        if (barW > 0) drawRoundedRect(ctx, x, y, barW, h, 2, fgColor);
    }

    public static void drawShadowText(DrawContext ctx, net.minecraft.client.font.TextRenderer tr, String text, int x, int y, int color) {
        ctx.drawText(tr, text, x + 1, y + 1, 0x55000000, false);
        ctx.drawText(tr, text, x, y, color, false);
    }

    public static int interpolateColor(int c1, int c2, float f) {
        f = Math.max(0, Math.min(1, f));
        int a1 = (c1 >> 24) & 0xFF, r1 = (c1 >> 16) & 0xFF, g1 = (c1 >> 8) & 0xFF, b1 = c1 & 0xFF;
        int a2 = (c2 >> 24) & 0xFF, r2 = (c2 >> 16) & 0xFF, g2 = (c2 >> 8) & 0xFF, b2 = c2 & 0xFF;
        return ((int)(a1 + (a2 - a1) * f) << 24) | ((int)(r1 + (r2 - r1) * f) << 16) |
               ((int)(g1 + (g2 - g1) * f) << 8) | (int)(b1 + (b2 - b1) * f);
    }

    public static int rainbowColor(long offset, float sat, float bri) {
        float hue = ((System.currentTimeMillis() + offset) % 3000) / 3000.0f;
        return java.awt.Color.HSBtoRGB(hue, sat, bri);
    }

    public static int pulseColor(int color, float speed, float minAlpha, float maxAlpha) {
        float pulse = (float)(Math.sin(System.currentTimeMillis() / (double)speed) * 0.5 + 0.5);
        int alpha = (int)(minAlpha + pulse * (maxAlpha - minAlpha));
        return withAlpha(color, alpha);
    }

    public static int withAlpha(int color, int alpha) {
        return (Math.max(0, Math.min(255, alpha)) << 24) | (color & 0x00FFFFFF);
    }

    public static int getHealthColor(float health, float maxHealth) {
        float pct = health / maxHealth;
        if (pct > 0.6f) return 0xFF00FF00;
        if (pct > 0.3f) return 0xFFFFFF00;
        return 0xFFFF0000;
    }

    public static void drawCircle(DrawContext ctx, int cx, int cy, int radius, int segments, int color) {
        for (int i = 0; i < segments; i++) {
            double a1 = 2 * Math.PI * i / segments;
            double a2 = 2 * Math.PI * (i + 1) / segments;
            int x1 = cx + (int)(Math.cos(a1) * radius);
            int y1 = cy + (int)(Math.sin(a1) * radius);
            int x2 = cx + (int)(Math.cos(a2) * radius);
            int y2 = cy + (int)(Math.sin(a2) * radius);
            ctx.fill(Math.min(x1, x2), Math.min(y1, y2),
                     Math.max(x1, x2) + 1, Math.max(y1, y2) + 1, color);
        }
    }
}
