package com.alpine.visuals.client.gui;

import com.alpine.visuals.client.AlpineVisualsClient;
import com.alpine.visuals.client.render.RenderUtils;
import com.alpine.visuals.client.visual.VisualManager;
import com.alpine.visuals.client.visual.VisualModule;
import com.alpine.visuals.client.visual.VisualModule.Category;
import com.alpine.visuals.client.gui.components.ModuleButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;

public class VisualMenuScreen extends Screen {
    private VisualManager vm;
    private Category selectedCat = Category.RENDER;
    private final List<ModuleButton> buttons = new ArrayList<>();
    private float openAnim = 0;
    private float scroll = 0, targetScroll = 0;

    private static final int PW = 380, PH = 320;
    private static final int TAB_H = 28, MOD_H = 42, PAD = 8;

    public VisualMenuScreen() { super(Text.literal("Alpine Visuals")); }

    @Override
    protected void init() {
        vm = AlpineVisualsClient.getInstance().getVisualManager();
        openAnim = 0;
        rebuildButtons();
    }

    private void rebuildButtons() {
        buttons.clear();
        List<VisualModule> mods = vm.getModulesByCategory(selectedCat);
        int px = (width - PW) / 2;
        int py = (height - PH) / 2;
        int cy = py + TAB_H + 44;
        for (int i = 0; i < mods.size(); i++) {
            buttons.add(new ModuleButton(mods.get(i), px + PAD, cy + i * (MOD_H + 3),
                PW - PAD * 2, MOD_H, i));
        }
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        openAnim += (1.0f - openAnim) * 0.12f;
        scroll += (targetScroll - scroll) * 0.2f;
        int alpha = (int)(Math.min(openAnim, 1.0f) * 255);

        // Dim background with blur-like gradient
        ctx.fill(0, 0, width, height, RenderUtils.withAlpha(0x000000, (int)(alpha * 0.65f)));

        int px = (width - PW) / 2;
        int py = (height - PH) / 2;

        drawPanel(ctx, px, py, PW, PH, alpha);
        drawTitle(ctx, px, py, PW, alpha);
        drawTabs(ctx, px, py + 30, PW, mx, my, alpha);

        int cy = py + TAB_H + 44;
        int ch = PH - TAB_H - 56;

        ctx.enableScissor(px, cy, px + PW, cy + ch);
        for (ModuleButton b : buttons) {
            int adjY = b.getBaseY() + (int) scroll;
            if (adjY + MOD_H > cy - 5 && adjY < cy + ch + 5) {
                b.render(ctx, mx, my, delta, (int) scroll, alpha);
            }
        }
        ctx.disableScissor();

        // Scrollbar
        if (!buttons.isEmpty()) {
            int totalH = buttons.size() * (MOD_H + 3);
            if (totalH > ch) {
                float ratio = (float) ch / totalH;
                int barH = Math.max((int)(ch * ratio), 15);
                float scrollRatio = totalH > ch ? -scroll / (totalH - ch) : 0;
                int barY = cy + (int)(scrollRatio * (ch - barH));
                int barColor = RenderUtils.rainbowColor(0, 0.3f, 0.6f);
                RenderUtils.drawRoundedRect(ctx, px + PW - 4, barY, 3, barH, 1,
                    RenderUtils.withAlpha(barColor, (int)(alpha * 0.6f)));
            }
        }

        // Footer
        String[] hints = {"RIGHT SHIFT", "Close", "|", "Click", "Toggle", "|", "Scroll", "Navigate"};
        StringBuilder hint = new StringBuilder();
        for (int i = 0; i < hints.length; i += 2) {
            if (i > 0) hint.append("  ");
            if (hints[i].equals("|")) { hint.append("  "); continue; }
            hint.append(hints[i]).append(": ").append(hints[i+1]);
        }
        String h = hint.toString();
        int hw = textRenderer.getWidth(h);
        ctx.drawText(textRenderer, h, px + (PW - hw) / 2, py + PH - 12,
            RenderUtils.withAlpha(0x666666, alpha), false);

        super.render(ctx, mx, my, delta);
    }

    private void drawPanel(DrawContext ctx, int x, int y, int w, int h, int a) {
        int glow = RenderUtils.rainbowColor(0, 0.6f, 0.8f);
        RenderUtils.drawGlowOutline(ctx, x, y, w, h, RenderUtils.withAlpha(glow, (int)(a * 0.25f)), 5);
        RenderUtils.drawRoundedRect(ctx, x, y, w, h, 5, RenderUtils.withAlpha(0x0A0A14, a));

        // Top accent gradient
        int ac = RenderUtils.rainbowColor(0, 0.5f, 0.7f);
        ctx.fillGradient(x + 1, y + 1, x + w - 1, y + 4,
            RenderUtils.withAlpha(ac, (int)(a * 0.8f)), RenderUtils.withAlpha(ac, 0));

        // Border
        RenderUtils.drawOutline(ctx, x, y, w, h, RenderUtils.withAlpha(0x2A2A44, a));
    }

    private void drawTitle(DrawContext ctx, int x, int y, int w, int a) {
        String title = "ALPINE VISUALS";
        String sub = "v2.0 | Ultimate Visual Suite";
        int tw = textRenderer.getWidth(title);
        int sw2 = textRenderer.getWidth(sub);
        int tc = RenderUtils.rainbowColor(0, 0.4f, 1.0f);
        ctx.drawText(textRenderer, title, x + (w - tw) / 2, y + 7,
            RenderUtils.withAlpha(tc & 0x00FFFFFF, a), true);
        ctx.drawText(textRenderer, sub, x + (w - sw2) / 2, y + 18,
            RenderUtils.withAlpha(0x666688, a), false);

        // Rainbow line
        int ly = y + 28;
        for (int i = 0; i < w - 16; i++) {
            float p = (float) i / (w - 16);
            int lc = RenderUtils.rainbowColor((long)(p * 2000), 0.6f, 0.7f);
            float edge = Math.min(p * 15, (1 - p) * 15);
            edge = Math.min(edge, 1.0f);
            ctx.fill(x + 8 + i, ly, x + 9 + i, ly + 1,
                RenderUtils.withAlpha(lc, (int)(a * edge * 0.7f)));
        }
    }

    private void drawTabs(DrawContext ctx, int px, int ty, int pw, int mx, int my, int a) {
        Category[] cats = Category.values();
        int tabW = (pw - PAD * 2) / cats.length;
        for (int i = 0; i < cats.length; i++) {
            Category c = cats[i];
            int tx = px + PAD + i * tabW;
            int tw = tabW - 2;
            boolean hov = mx >= tx && mx < tx + tw && my >= ty && my < ty + TAB_H;
            boolean sel = c == selectedCat;

            int bg;
            if (sel) bg = RenderUtils.withAlpha(c.color & 0x00FFFFFF, (int)(a * 0.25f));
            else if (hov) bg = RenderUtils.withAlpha(0xFFFFFF, (int)(a * 0.07f));
            else bg = RenderUtils.withAlpha(0xFFFFFF, (int)(a * 0.02f));

            RenderUtils.drawRoundedRect(ctx, tx, ty + 3, tw, TAB_H - 6, 4, bg);

            if (sel) {
                // Animated bottom indicator
                float pulse = (float)(Math.sin(System.currentTimeMillis() / 400.0) * 0.3 + 0.7);
                int ic = RenderUtils.withAlpha(c.color, (int)(a * pulse));
                ctx.fill(tx + 6, ty + TAB_H - 4, tx + tw - 6, ty + TAB_H - 2, ic);

                // Glow on selected
                RenderUtils.drawGlowOutline(ctx, tx, ty + 3, tw, TAB_H - 6,
                    RenderUtils.withAlpha(c.color, (int)(a * 0.15f)), 2);
            }

            String name = c.displayName;
            int textX = tx + (tw - textRenderer.getWidth(name)) / 2;
            int textC = sel ? c.color : (hov ? 0xBBBBBB : 0x777777);
            ctx.drawText(textRenderer, name, textX, ty + 9,
                RenderUtils.withAlpha(textC & 0x00FFFFFF, a), sel);

            // Module count badge
            int count = vm.getModulesByCategory(c).size();
            String badge = String.valueOf(count);
            int bw = textRenderer.getWidth(badge) + 4;
            ctx.drawText(textRenderer, badge, tx + tw - bw, ty + 4,
                RenderUtils.withAlpha(0x555555, a), false);
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        if (btn == 0) {
            int px = (width - PW) / 2;
            int py = (height - PH) / 2;
            int ty = py + 30;

            Category[] cats = Category.values();
            int tabW = (PW - PAD * 2) / cats.length;
            for (int i = 0; i < cats.length; i++) {
                int tx = px + PAD + i * tabW;
                int tw = tabW - 2;
                if (mx >= tx && mx < tx + tw && my >= ty && my < ty + TAB_H) {
                    selectedCat = cats[i];
                    scroll = 0; targetScroll = 0;
                    rebuildButtons();
                    return true;
                }
            }
            for (ModuleButton b : buttons) {
                if (b.isHovered((int) mx, (int) my, (int) scroll)) {
                    b.getModule().toggle();
                    AlpineVisualsClient.getInstance().getConfigManager().save(vm);
                    return true;
                }
            }
        }
        return super.mouseClicked(mx, my, btn);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double amt) {
        targetScroll += (float)(amt * 35);
        int py = (height - PH) / 2;
        int ch = PH - TAB_H - 56;
        int totalH = buttons.size() * (MOD_H + 3);
        if (targetScroll > 0) targetScroll = 0;
        if (totalH > ch && targetScroll < -(totalH - ch)) targetScroll = -(totalH - ch);
        if (totalH <= ch) targetScroll = 0;
        return true;
    }

    @Override public boolean shouldPause() { return false; }

    @Override
    public void close() {
        AlpineVisualsClient.getInstance().getConfigManager().save(vm);
        super.close();
    }
}
