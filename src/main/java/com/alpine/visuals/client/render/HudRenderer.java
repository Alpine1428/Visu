package com.alpine.visuals.client.render;

import com.alpine.visuals.client.visual.VisualManager;
import com.alpine.visuals.client.visual.VisualModule;
import com.alpine.visuals.client.visual.modules.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class HudRenderer {
    private final VisualManager vm;
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public HudRenderer(VisualManager vm) { this.vm = vm; }

    public void render(DrawContext ctx, float td) {
        if (mc.player == null || mc.options.hudHidden) return;

        renderWatermark(ctx);
        renderActiveModules(ctx);

        if (HudInfoModule.active) renderHudInfo(ctx);
        if (CoordinatesModule.active) renderCoords(ctx);
        if (ArmorHudModule.active) renderArmor(ctx);
        if (CompassModule.active) renderCompass(ctx);
        if (PotionTimerModule.active) renderPotions(ctx);
        if (CustomCrosshairModule.active) renderCrosshair(ctx);
        if (KeystrokesModule.active) renderKeystrokes(ctx);
        if (SpeedDisplayModule.active) renderSpeed(ctx);
        if (ClockModule.active) renderClock(ctx);
        if (CPSModule.active) renderCPS(ctx);
        if (TargetHudModule.active) renderTargetHud(ctx);
        if (ScreenEffectsModule.active) renderScreenFX(ctx);
    }

    private void renderWatermark(DrawContext ctx) {
        long t = System.currentTimeMillis();
        String text = "Alpine Visuals";
        int tw = mc.textRenderer.getWidth(text);
        int bgW = tw + 16;

        // Animated background
        int bgColor = RenderUtils.withAlpha(0x0D0D15, 220);
        RenderUtils.drawGlowRect(ctx, 4, 4, bgW, 18, bgColor, 3);

        // Rainbow accent line on top
        for (int i = 0; i < bgW; i++) {
            float p = (float) i / bgW;
            int c = RenderUtils.rainbowColor((long)(p * 1500), 0.7f, 0.9f);
            ctx.fill(4 + i, 4, 5 + i, 5, c);
        }

        // Gradient text
        int c1 = RenderUtils.rainbowColor(0, 0.5f, 1.0f);
        ctx.drawText(mc.textRenderer, text, 12, 9, c1, true);

        // Version
        String ver = "v2.0";
        int vc = RenderUtils.withAlpha(0x888888, 180);
        ctx.drawText(mc.textRenderer, ver, 14 + tw, 9, vc, false);
    }

    private void renderActiveModules(DrawContext ctx) {
        int sw = mc.getWindow().getScaledWidth();
        int y = 4;
        int idx = 0;
        for (VisualModule m : vm.getModules()) {
            if (!m.isEnabled()) continue;
            String name = m.getName();
            int tw = mc.textRenderer.getWidth(name);
            int x = sw - tw - 8;
            int rainbow = RenderUtils.rainbowColor(idx * 150L, 0.6f, 1.0f);

            // Animated slide-in background
            RenderUtils.drawRoundedRect(ctx, x - 6, y, tw + 14, 12, 2, 0x90000000);
            ctx.fill(sw - 2, y, sw, y + 12, rainbow);
            ctx.drawText(mc.textRenderer, name, x, y + 2, rainbow, true);
            y += 13;
            idx++;
        }
    }

    private void renderHudInfo(DrawContext ctx) {
        int y = 28;
        int x = 6;

        // FPS with color coding
        int fps = mc.getCurrentFps();
        int fpsColor = fps > 60 ? 0xFF00FF88 : fps > 30 ? 0xFFFFFF00 : 0xFFFF4444;
        drawInfoLine(ctx, x, y, "FPS", String.valueOf(fps), fpsColor);
        y += 13;

        // Ping
        PlayerListEntry pe = mc.player.networkHandler != null ?
            mc.player.networkHandler.getPlayerListEntry(mc.player.getUuid()) : null;
        int ping = pe != null ? pe.getLatency() : -1;
        int pingC = ping < 50 ? 0xFF00FF00 : ping < 100 ? 0xFFFFFF00 : ping < 200 ? 0xFFFF8800 : 0xFFFF0000;
        drawInfoLine(ctx, x, y, "Ping", ping >= 0 ? ping + "ms" : "N/A", pingC);
        y += 13;

        // Direction
        String dir = getDirection();
        drawInfoLine(ctx, x, y, "Dir", dir, 0xFFAAAAFF);
        y += 13;

        // Biome
        if (mc.world != null) {
            BlockPos pos = mc.player.getBlockPos();
            String biome = mc.world.getBiome(pos).getKey()
                .map(k -> capitalize(k.getValue().getPath().replace("_", " ")))
                .orElse("Unknown");
            drawInfoLine(ctx, x, y, "Biome", biome, 0xFF88DDAA);
            y += 13;
        }

        // Server
        if (mc.getCurrentServerEntry() != null) {
            String server = mc.getCurrentServerEntry().address;
            if (server.length() > 25) server = server.substring(0, 25) + "..";
            drawInfoLine(ctx, x, y, "Server", server, 0xFFCC88FF);
        }
    }

    private void drawInfoLine(DrawContext ctx, int x, int y, String label, String value, int valueColor) {
        String full = label + ": " + value;
        int w = mc.textRenderer.getWidth(full) + 8;
        RenderUtils.drawRoundedRect(ctx, x - 2, y - 1, w, 12, 2, 0x80000000);
        ctx.drawText(mc.textRenderer, label + ": ", x, y + 1, 0xFF999999, false);
        int labelW = mc.textRenderer.getWidth(label + ": ");
        ctx.drawText(mc.textRenderer, value, x + labelW, y + 1, valueColor, true);
    }

    private void renderCoords(DrawContext ctx) {
        int sh = mc.getWindow().getScaledHeight();
        int y = sh - 56;
        float hue = ((System.currentTimeMillis() % 5000) / 5000.0f);
        int color = java.awt.Color.HSBtoRGB(hue, 0.4f, 1.0f);

        String xyz = String.format("X: %.1f  Y: %.1f  Z: %.1f",
            mc.player.getX(), mc.player.getY(), mc.player.getZ());
        int w = mc.textRenderer.getWidth(xyz) + 12;
        RenderUtils.drawRoundedRect(ctx, 4, y, w, 14, 3, 0x90000000);
        ctx.drawText(mc.textRenderer, xyz, 10, y + 3, color, true);
        y += 15;

        // Nether coords
        String nc = String.format("Nether: %.0f / %.0f", mc.player.getX() / 8, mc.player.getZ() / 8);
        RenderUtils.drawRoundedRect(ctx, 4, y, mc.textRenderer.getWidth(nc) + 12, 14, 3, 0x90000000);
        ctx.drawText(mc.textRenderer, nc, 10, y + 3, 0xFFFF6666, true);
        y += 15;

        // Chunk coords
        String cc = String.format("Chunk: %d / %d",
            mc.player.getBlockPos().getX() >> 4, mc.player.getBlockPos().getZ() >> 4);
        RenderUtils.drawRoundedRect(ctx, 4, y, mc.textRenderer.getWidth(cc) + 12, 14, 3, 0x90000000);
        ctx.drawText(mc.textRenderer, cc, 10, y + 3, 0xFF88AAFF, true);
    }

    private void renderArmor(DrawContext ctx) {
        int sw = mc.getWindow().getScaledWidth();
        int sh = mc.getWindow().getScaledHeight();
        int x = sw / 2 + 98;
        int y = sh - 55;

        for (int i = 3; i >= 0; i--) {
            ItemStack stack = mc.player.getInventory().getArmorStack(i);
            if (!stack.isEmpty()) {
                ctx.drawItem(stack, x, y);
                if (stack.isDamageable()) {
                    float pct = 1.0f - (float)stack.getDamage() / stack.getMaxDamage();
                    int bc = pct > 0.6f ? 0xFF00FF00 : pct > 0.3f ? 0xFFFFFF00 : 0xFFFF0000;
                    int bw = (int)(14 * pct);
                    ctx.fill(x + 1, y + 14, x + 15, y + 16, 0xFF000000);
                    ctx.fill(x + 1, y + 14, x + 1 + bw, y + 16, bc);
                    // Percentage text
                    String pctStr = (int)(pct * 100) + "%";
                    ctx.drawText(mc.textRenderer, pctStr, x + 17, y + 5, bc, true);
                }
                y -= 20;
            }
        }
    }

    private void renderCompass(DrawContext ctx) {
        int sw = mc.getWindow().getScaledWidth();
        int cx = sw / 2;
        int y = 4;
        int w = 220;
        float yaw = mc.player.getYaw() % 360;
        if (yaw < 0) yaw += 360;

        RenderUtils.drawRoundedRect(ctx, cx - w / 2, y, w, 18, 4, 0x90000000);

        String[] dirs = {"S", "SW", "W", "NW", "N", "NE", "E", "SE"};
        float[] angles = {0, 45, 90, 135, 180, 225, 270, 315};
        int[] colors = {0xFFFF4444, 0xFF888888, 0xFFFFFFFF, 0xFF888888,
                        0xFF4444FF, 0xFF888888, 0xFFFFFFFF, 0xFF888888};

        for (int i = 0; i < dirs.length; i++) {
            float diff = angles[i] - yaw;
            if (diff > 180) diff -= 360;
            if (diff < -180) diff += 360;
            int dx = cx + (int)(diff * (w / 200.0f));
            if (dx > cx - w / 2 + 8 && dx < cx + w / 2 - 12) {
                ctx.drawText(mc.textRenderer, dirs[i],
                    dx - mc.textRenderer.getWidth(dirs[i]) / 2, y + 5, colors[i], dirs[i].length() == 1);
            }
        }

        // Degree display
        String deg = String.format("%.0f", yaw) + "°";
        int dw = mc.textRenderer.getWidth(deg);
        ctx.drawText(mc.textRenderer, deg, cx - dw / 2, y + 19, 0xFFAAAAAA, false);

        // Center marker
        ctx.fill(cx, y + 1, cx + 1, y + 4, 0xFFFF4444);
        ctx.fill(cx, y + 14, cx + 1, y + 17, 0xFFFF4444);
    }

    private void renderPotions(DrawContext ctx) {
        Collection<StatusEffectInstance> effects = mc.player.getStatusEffects();
        if (effects.isEmpty()) return;
        int sw = mc.getWindow().getScaledWidth();
        int sh = mc.getWindow().getScaledHeight();
        int y = sh - 50;

        for (StatusEffectInstance e : effects) {
            String name = e.getEffectType().getName().getString();
            int amp = e.getAmplifier() + 1;
            int dur = e.getDuration();
            int mins = (dur / 20) / 60;
            int secs = (dur / 20) % 60;
            boolean infinite = dur > 72000;
            String timeStr = infinite ? "INF" : String.format("%d:%02d", mins, secs);
            String text = name + " " + toRoman(amp) + "  " + timeStr;
            int tw = mc.textRenderer.getWidth(text);
            int x = sw - tw - 8;
            int ec = e.getEffectType().getColor();

            RenderUtils.drawRoundedRect(ctx, x - 4, y, tw + 12, 13, 2, 0x85000000);
            ctx.fill(x - 4, y, x - 1, y + 13, ec | 0xFF000000);

            // Flash when about to expire
            int textColor = 0xFFDDDDDD;
            if (!infinite && dur < 200) {
                textColor = RenderUtils.pulseColor(0xFFFF4444, 200, 100, 255);
            }
            ctx.drawText(mc.textRenderer, text, x, y + 3, textColor, true);
            y -= 15;
        }
    }

    private void renderCrosshair(DrawContext ctx) {
        int sw = mc.getWindow().getScaledWidth();
        int sh = mc.getWindow().getScaledHeight();
        int cx = sw / 2;
        int cy = sh / 2;
        long t = System.currentTimeMillis();

        float pulse = (float)(Math.sin(t / 300.0) * 0.3 + 0.7);
        int alpha = (int)(pulse * 255);
        int color = RenderUtils.withAlpha(0xFFFFFF, alpha);
        int accent = RenderUtils.rainbowColor(0, 0.5f, 1.0f);

        // Dynamic gap based on movement
        int baseGap = 3;
        int gap = baseGap;
        if (mc.player != null) {
            double speed = Math.sqrt(
                mc.player.getVelocity().x * mc.player.getVelocity().x +
                mc.player.getVelocity().z * mc.player.getVelocity().z
            );
            gap = baseGap + (int)(speed * 15);
        }

        int size = 10;
        int thick = 1;

        // Main lines
        ctx.fill(cx - size, cy, cx - gap, cy + thick, color);
        ctx.fill(cx + gap + 1, cy, cx + size + 1, cy + thick, color);
        ctx.fill(cx, cy - size, cx + thick, cy - gap, color);
        ctx.fill(cx, cy + gap + 1, cx + thick, cy + size + 1, color);

        // Center dot
        ctx.fill(cx, cy, cx + 1, cy + 1, accent);

        // Animated corners
        int cs = 3;
        int ca = (int)(pulse * 200);
        int cc = RenderUtils.withAlpha(accent, ca);

        // Top-left
        ctx.fill(cx - gap, cy - gap, cx - gap + cs, cy - gap + 1, cc);
        ctx.fill(cx - gap, cy - gap, cx - gap + 1, cy - gap + cs, cc);
        // Top-right
        ctx.fill(cx + gap - cs + 1, cy - gap, cx + gap + 1, cy - gap + 1, cc);
        ctx.fill(cx + gap, cy - gap, cx + gap + 1, cy - gap + cs, cc);
        // Bottom-left
        ctx.fill(cx - gap, cy + gap, cx - gap + cs, cy + gap + 1, cc);
        ctx.fill(cx - gap, cy + gap - cs + 1, cx - gap + 1, cy + gap + 1, cc);
        // Bottom-right
        ctx.fill(cx + gap - cs + 1, cy + gap, cx + gap + 1, cy + gap + 1, cc);
        ctx.fill(cx + gap, cy + gap - cs + 1, cx + gap + 1, cy + gap + 1, cc);

        // Kill indicator ring (when hitting entity)
        if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
            float ring = (float)(Math.sin(t / 150.0) * 0.5 + 0.5);
            int ringA = (int)(ring * 180);
            int ringC = RenderUtils.withAlpha(0xFF4444, ringA);
            for (int i = 0; i < 8; i++) {
                double angle = t / 500.0 + i * Math.PI / 4;
                int rx = cx + (int)(Math.cos(angle) * (gap + 5));
                int ry = cy + (int)(Math.sin(angle) * (gap + 5));
                ctx.fill(rx, ry, rx + 1, ry + 1, ringC);
            }
        }
    }

    private void renderKeystrokes(DrawContext ctx) {
        int sw = mc.getWindow().getScaledWidth();
        int sh = mc.getWindow().getScaledHeight();
        int baseX = sw - 75;
        int baseY = sh - 85;
        int ks = 20;
        int gap = 2;

        boolean w = mc.options.forwardKey.isPressed();
        boolean a = mc.options.leftKey.isPressed();
        boolean s = mc.options.backKey.isPressed();
        boolean d = mc.options.rightKey.isPressed();
        boolean space = mc.options.jumpKey.isPressed();
        boolean lmb = GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        boolean rmb = GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;

        int activeColor = 0xCC6A5ACD;
        int inactiveColor = 0x60000000;

        // W
        drawKey(ctx, baseX + ks + gap, baseY, ks, ks, "W", w, activeColor, inactiveColor);
        // A
        drawKey(ctx, baseX, baseY + ks + gap, ks, ks, "A", a, activeColor, inactiveColor);
        // S
        drawKey(ctx, baseX + ks + gap, baseY + ks + gap, ks, ks, "S", s, activeColor, inactiveColor);
        // D
        drawKey(ctx, baseX + (ks + gap) * 2, baseY + ks + gap, ks, ks, "D", d, activeColor, inactiveColor);

        // Space
        int spaceW = ks * 3 + gap * 2;
        drawKey(ctx, baseX, baseY + (ks + gap) * 2, spaceW, 12, "---", space, activeColor, inactiveColor);

        // LMB / RMB
        int mbW = (spaceW - gap) / 2;
        drawKey(ctx, baseX, baseY + (ks + gap) * 2 + 14, mbW, 14, "LMB", lmb, 0xCC22C55E, inactiveColor);
        drawKey(ctx, baseX + mbW + gap, baseY + (ks + gap) * 2 + 14, mbW, 14, "RMB", rmb, 0xCCEF4444, inactiveColor);
    }

    private void drawKey(DrawContext ctx, int x, int y, int w, int h, String label, boolean pressed, int activeC, int inactiveC) {
        int bg = pressed ? activeC : inactiveC;
        RenderUtils.drawRoundedRect(ctx, x, y, w, h, 3, bg);
        if (pressed) RenderUtils.drawOutline(ctx, x, y, w, h, RenderUtils.withAlpha(0xFFFFFF, 60));
        int tw = mc.textRenderer.getWidth(label);
        int textC = pressed ? 0xFFFFFFFF : 0xFF999999;
        ctx.drawText(mc.textRenderer, label, x + (w - tw) / 2, y + (h - 8) / 2, textC, pressed);
    }

    private void renderSpeed(DrawContext ctx) {
        int sw = mc.getWindow().getScaledWidth();
        int sh = mc.getWindow().getScaledHeight();
        String text = String.format("%.1f b/s", SpeedDisplayModule.currentSpeed);
        int tw = mc.textRenderer.getWidth(text);
        int x = sw / 2 - tw / 2;
        int y = sh / 2 + 20;
        RenderUtils.drawRoundedRect(ctx, x - 4, y - 1, tw + 8, 12, 2, 0x80000000);

        int color = SpeedDisplayModule.currentSpeed > 8 ? 0xFFFF8800 :
                    SpeedDisplayModule.currentSpeed > 5 ? 0xFF00FF88 : 0xFFAAAAFF;
        ctx.drawText(mc.textRenderer, text, x, y + 1, color, true);
    }

    private void renderClock(DrawContext ctx) {
        int sw = mc.getWindow().getScaledWidth();
        LocalTime now = LocalTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        int tw = mc.textRenderer.getWidth(time);

        // Real time
        RenderUtils.drawRoundedRect(ctx, sw - tw - 12, 4, tw + 8, 12, 2, 0x80000000);
        ctx.drawText(mc.textRenderer, time, sw - tw - 8, 6, 0xFFCCCCCC, false);

        // In-game time
        if (mc.world != null) {
            long worldTime = mc.world.getTimeOfDay() % 24000;
            int hours = (int)((worldTime / 1000 + 6) % 24);
            int minutes = (int)((worldTime % 1000) * 60 / 1000);
            String igTime = String.format("MC %02d:%02d", hours, minutes);
            int igw = mc.textRenderer.getWidth(igTime);
            RenderUtils.drawRoundedRect(ctx, sw - igw - 12, 18, igw + 8, 12, 2, 0x80000000);

            int timeColor = (hours >= 6 && hours < 18) ? 0xFFFFDD44 : 0xFF4466FF;
            ctx.drawText(mc.textRenderer, igTime, sw - igw - 8, 20, timeColor, false);
        }
    }

    private void renderCPS(DrawContext ctx) {
        int sw = mc.getWindow().getScaledWidth();
        int sh = mc.getWindow().getScaledHeight();
        String text = CPSModule.leftCPS + " | " + CPSModule.rightCPS + " CPS";
        int tw = mc.textRenderer.getWidth(text);
        int x = sw / 2 - tw / 2;
        int y = sh / 2 + 34;
        RenderUtils.drawRoundedRect(ctx, x - 4, y - 1, tw + 8, 12, 2, 0x80000000);
        ctx.drawText(mc.textRenderer, text, x, y + 1, 0xFFDDDDDD, true);
    }

    private void renderTargetHud(DrawContext ctx) {
        if (mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.ENTITY) return;
        Entity target = ((EntityHitResult) mc.crosshairTarget).getEntity();
        if (!(target instanceof LivingEntity living)) return;

        int sw = mc.getWindow().getScaledWidth();
        int sh = mc.getWindow().getScaledHeight();
        int w = 140;
        int h = 44;
        int x = sw / 2 + 20;
        int y = sh / 2 - h / 2;

        // Background with glow
        int accentColor = RenderUtils.rainbowColor(0, 0.5f, 0.8f);
        RenderUtils.drawGlowRect(ctx, x, y, w, h, 0xCC0D0D15, 3);

        // Name
        String name = target.getName().getString();
        ctx.drawText(mc.textRenderer, name, x + 6, y + 5, 0xFFFFFFFF, true);

        // Health bar
        float health = living.getHealth();
        float maxHealth = living.getMaxHealth();
        float pct = health / maxHealth;
        int hbColor = RenderUtils.getHealthColor(health, maxHealth);
        RenderUtils.drawProgressBar(ctx, x + 6, y + 18, w - 12, 8, pct, 0xFF333333, hbColor);

        // Health text
        String hpText = String.format("%.1f / %.1f", health, maxHealth);
        int htw = mc.textRenderer.getWidth(hpText);
        ctx.drawText(mc.textRenderer, hpText, x + (w - htw) / 2, y + 19, 0xFFFFFFFF, true);

        // Distance
        double dist = mc.player.distanceTo(target);
        String distStr = String.format("%.1fm", dist);
        ctx.drawText(mc.textRenderer, distStr, x + 6, y + 30, 0xFF999999, false);

        // Accent line
        ctx.fill(x, y, x + (int)(w * pct), y + 2, accentColor);
    }

    private void renderScreenFX(DrawContext ctx) {
        int sw = mc.getWindow().getScaledWidth();
        int sh = mc.getWindow().getScaledHeight();

        // Vignette effect
        int vigSize = 60;
        for (int i = 0; i < vigSize; i++) {
            int alpha = (int)((1.0f - (float)i / vigSize) * 80);
            int c = RenderUtils.withAlpha(0x000000, alpha);
            // Top
            ctx.fill(0, i, sw, i + 1, c);
            // Bottom
            ctx.fill(0, sh - i - 1, sw, sh - i, c);
            // Left
            ctx.fill(i, 0, i + 1, sh, c);
            // Right
            ctx.fill(sw - i - 1, 0, sw - i, sh, c);
        }
    }

    private String getDirection() {
        float yaw = mc.player.getYaw() % 360;
        if (yaw < 0) yaw += 360;
        if (yaw >= 315 || yaw < 45) return "South";
        if (yaw < 135) return "West";
        if (yaw < 225) return "North";
        return "East";
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        for (String w : s.split(" ")) {
            if (!w.isEmpty()) sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    private String toRoman(int n) {
        return switch (n) {
            case 1 -> "I"; case 2 -> "II"; case 3 -> "III";
            case 4 -> "IV"; case 5 -> "V"; default -> String.valueOf(n);
        };
    }
}
