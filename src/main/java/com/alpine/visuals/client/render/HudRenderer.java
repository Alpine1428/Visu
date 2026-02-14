package com.alpine.visuals.client.render;

import com.alpine.visuals.client.visual.VisualManager;
import com.alpine.visuals.client.visual.VisualModule;
import com.alpine.visuals.client.visual.modules.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import java.util.Collection;

public class HudRenderer {
    private final VisualManager visualManager;
    private final MinecraftClient client = MinecraftClient.getInstance();

    public HudRenderer(VisualManager visualManager) {
        this.visualManager = visualManager;
    }

    public void render(DrawContext context, float tickDelta) {
        if (client.player == null || client.options.hudHidden) return;

        if (HudInfoModule.active) renderHudInfo(context);
        if (CoordinatesModule.active) renderCoordinates(context);
        if (ArmorHudModule.active) renderArmorHud(context);
        if (CompassModule.active) renderCompass(context);
        if (PotionTimerModule.active) renderPotionTimers(context);
        if (CustomCrosshairModule.active) renderCustomCrosshair(context);

        renderWatermark(context);
        renderActiveModules(context);
    }

    private void renderWatermark(DrawContext context) {
        long time = System.currentTimeMillis();
        int color1 = RenderUtils.rainbowColor(0, 0.7f, 1.0f);
        int color2 = RenderUtils.rainbowColor(300, 0.7f, 1.0f);
        String text = "Alpine Visuals";
        int tw = client.textRenderer.getWidth(text);
        RenderUtils.drawRoundedRect(context, 4, 4, tw + 12, 16, 3, 0x90000000);
        context.drawText(client.textRenderer, text, 10, 8, color1, true);

        float glowPos = (float)(Math.sin(time / 500.0) * 0.5 + 0.5);
        int glowX = (int)(4 + glowPos * (tw + 8 - 30));
        for (int i = 0; i < 30; i++) {
            float alpha = 1.0f - Math.abs(i - 15) / 15.0f;
            int c = RenderUtils.withAlpha(color2, (int)(alpha * 200));
            context.fill(glowX + i, 20, glowX + i + 1, 21, c);
        }
    }

    private void renderActiveModules(DrawContext context) {
        int screenW = client.getWindow().getScaledWidth();
        int y = 4;
        int idx = 0;
        for (VisualModule module : visualManager.getModules()) {
            if (!module.isEnabled()) continue;
            String name = module.getName();
            int textWidth = client.textRenderer.getWidth(name);
            int x = screenW - textWidth - 10;
            int rainbow = RenderUtils.rainbowColor(idx * 200L, 0.5f, 1.0f);
            RenderUtils.drawRoundedRect(context, x - 4, y - 1, textWidth + 12, 12, 2, 0x80000000);
            context.fill(screenW - 2, y - 1, screenW, y + 11, rainbow);
            context.drawText(client.textRenderer, name, x, y + 1, rainbow, true);
            y += 14;
            idx++;
        }
    }

    private void renderHudInfo(DrawContext context) {
        int y = 26;
        String fps = "FPS: " + client.getCurrentFps();
        RenderUtils.drawRoundedRect(context, 4, y, client.textRenderer.getWidth(fps) + 8, 12, 2, 0x80000000);
        context.drawText(client.textRenderer, fps, 8, y + 2, 0xFF00FF88, true);
        y += 14;

        PlayerListEntry entry = null;
        if (client.player.networkHandler != null) {
            entry = client.player.networkHandler.getPlayerListEntry(client.player.getUuid());
        }
        int ping = entry != null ? entry.getLatency() : -1;
        String pingStr = "Ping: " + (ping >= 0 ? ping + "ms" : "N/A");
        int pingColor = ping < 50 ? 0xFF00FF00 : ping < 100 ? 0xFFFFFF00 : ping < 200 ? 0xFFFF8800 : 0xFFFF0000;
        RenderUtils.drawRoundedRect(context, 4, y, client.textRenderer.getWidth(pingStr) + 8, 12, 2, 0x80000000);
        context.drawText(client.textRenderer, pingStr, 8, y + 2, pingColor, true);
        y += 14;

        String dir = getDirection();
        RenderUtils.drawRoundedRect(context, 4, y, client.textRenderer.getWidth(dir) + 8, 12, 2, 0x80000000);
        context.drawText(client.textRenderer, dir, 8, y + 2, 0xFFAAAAFF, true);
        y += 14;

        if (client.world != null) {
            BlockPos pos = client.player.getBlockPos();
            String biome = client.world.getBiome(pos).getKey()
                .map(key -> key.getValue().getPath().replace("_", " "))
                .orElse("Unknown");
            String biomeStr = "Biome: " + capitalize(biome);
            RenderUtils.drawRoundedRect(context, 4, y, client.textRenderer.getWidth(biomeStr) + 8, 12, 2, 0x80000000);
            context.drawText(client.textRenderer, biomeStr, 8, y + 2, 0xFF88DDAA, true);
        }
    }

    private void renderCoordinates(DrawContext context) {
        int screenH = client.getWindow().getScaledHeight();
        int y = screenH - 40;
        String coords = String.format("XYZ: %.1f / %.1f / %.1f",
            client.player.getX(), client.player.getY(), client.player.getZ());
        int bgW = client.textRenderer.getWidth(coords) + 12;
        RenderUtils.drawRoundedRect(context, 4, y, bgW, 14, 3, 0x90000000);
        float hue = ((System.currentTimeMillis() % 5000) / 5000.0f);
        int color = java.awt.Color.HSBtoRGB(hue, 0.4f, 1.0f);
        context.drawText(client.textRenderer, coords, 10, y + 3, color, true);

        String nc = String.format("Nether: %.0f / %.0f",
            client.player.getX() / 8.0, client.player.getZ() / 8.0);
        y += 16;
        RenderUtils.drawRoundedRect(context, 4, y, client.textRenderer.getWidth(nc) + 12, 14, 3, 0x90000000);
        context.drawText(client.textRenderer, nc, 10, y + 3, 0xFFFF6666, true);
    }

    private void renderArmorHud(DrawContext context) {
        int screenW = client.getWindow().getScaledWidth();
        int screenH = client.getWindow().getScaledHeight();
        int x = screenW / 2 + 98;
        int y = screenH - 55;
        for (int i = 3; i >= 0; i--) {
            ItemStack stack = client.player.getInventory().getArmorStack(i);
            if (!stack.isEmpty()) {
                context.drawItem(stack, x, y);
                if (stack.isDamageable()) {
                    int maxD = stack.getMaxDamage();
                    int dmg = stack.getDamage();
                    float pct = 1.0f - (float) dmg / maxD;
                    int bc = pct > 0.6f ? 0xFF00FF00 : pct > 0.3f ? 0xFFFFFF00 : 0xFFFF0000;
                    int bw = (int)(14 * pct);
                    context.fill(x + 1, y + 14, x + 15, y + 16, 0xFF000000);
                    context.fill(x + 1, y + 14, x + 1 + bw, y + 16, bc);
                }
                y -= 18;
            }
        }
    }

    private void renderCompass(DrawContext context) {
        int screenW = client.getWindow().getScaledWidth();
        int cx = screenW / 2;
        int y = 4;
        int w = 200;
        float yaw = client.player.getYaw() % 360;
        if (yaw < 0) yaw += 360;
        RenderUtils.drawRoundedRect(context, cx - w / 2, y, w, 16, 3, 0x90000000);
        String[] dirs = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        float[] angles = {180, 225, 270, 315, 0, 45, 90, 135};
        for (int i = 0; i < dirs.length; i++) {
            float diff = angles[i] - yaw;
            if (diff > 180) diff -= 360;
            if (diff < -180) diff += 360;
            int drawX = cx + (int)(diff * (w / 180.0f));
            if (drawX > cx - w / 2 + 5 && drawX < cx + w / 2 - 10) {
                boolean isCard = dirs[i].length() == 1;
                int c = isCard ? 0xFFFFFFFF : 0xFF888888;
                context.drawText(client.textRenderer, dirs[i],
                    drawX - client.textRenderer.getWidth(dirs[i]) / 2, y + 4, c, true);
            }
        }
        context.fill(cx, y + 1, cx + 1, y + 3, 0xFFFF4444);
        context.fill(cx, y + 13, cx + 1, y + 15, 0xFFFF4444);
    }

    private void renderPotionTimers(DrawContext context) {
        Collection<StatusEffectInstance> effects = client.player.getStatusEffects();
        if (effects.isEmpty()) return;
        int screenW = client.getWindow().getScaledWidth();
        int y = 30;
        for (StatusEffectInstance effect : effects) {
            String name = effect.getEffectType().getName().getString();
            int amp = effect.getAmplifier() + 1;
            int dur = effect.getDuration();
            int mins = (dur / 20) / 60;
            int secs = (dur / 20) % 60;
            String text = name + " " + amp + " - " + String.format("%d:%02d", mins, secs);
            int tw = client.textRenderer.getWidth(text);
            int x = screenW - tw - 10;
            int ec = effect.getEffectType().getColor();
            RenderUtils.drawRoundedRect(context, x - 4, y, tw + 12, 12, 2, 0x80000000);
            context.fill(x - 4, y, x - 2, y + 12, ec | 0xFF000000);
            context.drawText(client.textRenderer, text, x, y + 2, 0xFFDDDDDD, true);
            y += 14;
        }
    }

    private void renderCustomCrosshair(DrawContext context) {
        int screenW = client.getWindow().getScaledWidth();
        int screenH = client.getWindow().getScaledHeight();
        int cx = screenW / 2;
        int cy = screenH / 2;
        long time = System.currentTimeMillis();
        float pulse = (float)(Math.sin(time / 300.0) * 0.3 + 0.7);
        int alpha = (int)(pulse * 255);
        int color = RenderUtils.withAlpha(0x00FFFFFF, alpha);
        int accent = RenderUtils.rainbowColor(0, 0.5f, 1.0f);
        int size = 8, gap = 3;

        context.fill(cx - size, cy, cx - gap, cy + 1, color);
        context.fill(cx + gap + 1, cy, cx + size + 1, cy + 1, color);
        context.fill(cx, cy - size, cx + 1, cy - gap, color);
        context.fill(cx, cy + gap + 1, cx + 1, cy + size + 1, color);
        context.fill(cx, cy, cx + 1, cy + 1, accent);

        int cs = 2;
        int ca = (int)(pulse * 180);
        int cc = RenderUtils.withAlpha(accent, ca);
        context.fill(cx - gap, cy - gap, cx - gap + cs, cy - gap + 1, cc);
        context.fill(cx - gap, cy - gap, cx - gap + 1, cy - gap + cs, cc);
        context.fill(cx + gap - cs + 1, cy - gap, cx + gap + 1, cy - gap + 1, cc);
        context.fill(cx + gap, cy - gap, cx + gap + 1, cy - gap + cs, cc);
        context.fill(cx - gap, cy + gap, cx - gap + cs, cy + gap + 1, cc);
        context.fill(cx - gap, cy + gap - cs + 1, cx - gap + 1, cy + gap + 1, cc);
        context.fill(cx + gap - cs + 1, cy + gap, cx + gap + 1, cy + gap + 1, cc);
        context.fill(cx + gap, cy + gap - cs + 1, cx + gap + 1, cy + gap + 1, cc);
    }

    private String getDirection() {
        float yaw = client.player.getYaw() % 360;
        if (yaw < 0) yaw += 360;
        if (yaw >= 315 || yaw < 45) return "Facing: South (+Z)";
        else if (yaw >= 45 && yaw < 135) return "Facing: West (-X)";
        else if (yaw >= 135 && yaw < 225) return "Facing: North (-Z)";
        else return "Facing: East (+X)";
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        for (String w : s.split(" ")) {
            if (!w.isEmpty()) {
                sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
            }
        }
        return sb.toString().trim();
    }
}
