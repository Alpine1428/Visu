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
    private VisualManager visualManager;
    private Category selectedCategory = Category.RENDER;
    private final List<ModuleButton> moduleButtons = new ArrayList<>();
    private float openAnimation = 0;
    private float scrollOffset = 0;
    private float targetScroll = 0;

    private static final int PANEL_WIDTH = 340;
    private static final int PANEL_HEIGHT = 280;
    private static final int CATEGORY_TAB_HEIGHT = 32;
    private static final int MODULE_HEIGHT = 36;
    private static final int PADDING = 8;

    public VisualMenuScreen() {
        super(Text.literal("Alpine Visuals"));
    }

    @Override
    protected void init() {
        visualManager = AlpineVisualsClient.getInstance().getVisualManager();
        openAnimation = 0;
        updateModuleButtons();
    }

    private void updateModuleButtons() {
        moduleButtons.clear();
        List<VisualModule> modules = visualManager.getModulesByCategory(selectedCategory);
        int panelX = (width - PANEL_WIDTH) / 2;
        int panelY = (height - PANEL_HEIGHT) / 2;
        int contentY = panelY + CATEGORY_TAB_HEIGHT + 40;
        for (int i = 0; i < modules.size(); i++) {
            int btnY = contentY + i * (MODULE_HEIGHT + 4);
            moduleButtons.add(new ModuleButton(
                modules.get(i), panelX + PADDING, btnY,
                PANEL_WIDTH - PADDING * 2, MODULE_HEIGHT, i
            ));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        openAnimation += (1.0f - openAnimation) * 0.15f;
        scrollOffset += (targetScroll - scrollOffset) * 0.2f;
        float anim = Math.min(openAnimation, 1.0f);
        int alpha = (int)(anim * 255);

        context.fill(0, 0, width, height, RenderUtils.withAlpha(0x000000, (int)(alpha * 0.6f)));

        int panelX = (width - PANEL_WIDTH) / 2;
        int panelY = (height - PANEL_HEIGHT) / 2;

        drawPanelBackground(context, panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT, alpha);
        drawTitle(context, panelX, panelY, PANEL_WIDTH, alpha);
        drawCategoryTabs(context, panelX, panelY + 28, PANEL_WIDTH, mouseX, mouseY, alpha);

        int contentY = panelY + CATEGORY_TAB_HEIGHT + 40;
        int contentHeight = PANEL_HEIGHT - CATEGORY_TAB_HEIGHT - 52;

        context.enableScissor(panelX, contentY, panelX + PANEL_WIDTH, contentY + contentHeight);
        for (ModuleButton btn : moduleButtons) {
            int adjY = btn.getBaseY() + (int) scrollOffset;
            if (adjY + MODULE_HEIGHT > contentY && adjY < contentY + contentHeight) {
                btn.render(context, mouseX, mouseY, delta, (int) scrollOffset, alpha);
            }
        }
        context.disableScissor();

        String hint = "Right Shift - Close | Click to toggle";
        int hw = textRenderer.getWidth(hint);
        context.drawText(textRenderer, hint,
            panelX + (PANEL_WIDTH - hw) / 2,
            panelY + PANEL_HEIGHT - 14,
            RenderUtils.withAlpha(0x888888, alpha), false);

        super.render(context, mouseX, mouseY, delta);
    }

    private void drawPanelBackground(DrawContext ctx, int x, int y, int w, int h, int alpha) {
        int glowC1 = RenderUtils.rainbowColor(0, 0.6f, 0.8f);
        int glowC = RenderUtils.withAlpha(glowC1, (int)(alpha * 0.3f));
        RenderUtils.drawGlowOutline(ctx, x, y, w, h, glowC, 4);
        RenderUtils.drawRoundedRect(ctx, x, y, w, h, 4, RenderUtils.withAlpha(0x0D0D15, alpha));
        int ac = RenderUtils.rainbowColor(0, 0.5f, 0.7f);
        ctx.fillGradient(x + 1, y + 1, x + w - 1, y + 4,
            RenderUtils.withAlpha(ac, (int)(alpha * 0.8f)),
            RenderUtils.withAlpha(ac, 0));
        RenderUtils.drawOutline(ctx, x, y, w, h, RenderUtils.withAlpha(0x333355, alpha));
    }

    private void drawTitle(DrawContext ctx, int x, int y, int w, int alpha) {
        String title = "ALPINE VISUALS";
        int tw = textRenderer.getWidth(title);
        int tx = x + (w - tw) / 2;
        int tc = RenderUtils.rainbowColor(0, 0.4f, 1.0f);
        ctx.drawText(textRenderer, title, tx, y + 8, RenderUtils.withAlpha(tc & 0x00FFFFFF, alpha), true);

        int lineY = y + 22;
        int lineW = w - 20;
        int lineX = x + 10;
        for (int i = 0; i < lineW; i++) {
            float p = (float) i / lineW;
            int lc = RenderUtils.rainbowColor((long)(p * 1000), 0.5f, 0.6f);
            float edge = Math.min(p * 10, (1 - p) * 10);
            edge = Math.min(edge, 1.0f);
            ctx.fill(lineX + i, lineY, lineX + i + 1, lineY + 1,
                RenderUtils.withAlpha(lc, (int)(alpha * edge * 0.8f)));
        }
    }

    private void drawCategoryTabs(DrawContext ctx, int panelX, int tabY, int panelW, int mx, int my, int alpha) {
        Category[] cats = Category.values();
        int tabW = (panelW - PADDING * 2) / cats.length;
        for (int i = 0; i < cats.length; i++) {
            Category cat = cats[i];
            int tx = panelX + PADDING + i * tabW;
            int tw = tabW - 2;
            boolean hov = mx >= tx && mx < tx + tw && my >= tabY && my < tabY + CATEGORY_TAB_HEIGHT;
            boolean sel = cat == selectedCategory;
            int bgC;
            if (sel) {
                bgC = RenderUtils.withAlpha(cat.color & 0x00FFFFFF, (int)(alpha * 0.3f));
            } else if (hov) {
                bgC = RenderUtils.withAlpha(0xFFFFFF, (int)(alpha * 0.08f));
            } else {
                bgC = RenderUtils.withAlpha(0xFFFFFF, (int)(alpha * 0.03f));
            }
            RenderUtils.drawRoundedRect(ctx, tx, tabY + 4, tw, CATEGORY_TAB_HEIGHT - 8, 3, bgC);
            if (sel) {
                ctx.fill(tx + 4, tabY + CATEGORY_TAB_HEIGHT - 5, tx + tw - 4, tabY + CATEGORY_TAB_HEIGHT - 3,
                    RenderUtils.withAlpha(cat.color, alpha));
            }
            String name = cat.displayName;
            int textX = tx + (tw - textRenderer.getWidth(name)) / 2;
            int textC = sel ? cat.color : (hov ? 0xCCCCCC : 0x888888);
            ctx.drawText(textRenderer, name, textX, tabY + 12,
                RenderUtils.withAlpha(textC & 0x00FFFFFF, alpha), sel);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int panelX = (width - PANEL_WIDTH) / 2;
            int panelY = (height - PANEL_HEIGHT) / 2;
            int tabY = panelY + 28;
            Category[] cats = Category.values();
            int tabW = (PANEL_WIDTH - PADDING * 2) / cats.length;
            for (int i = 0; i < cats.length; i++) {
                int tx = panelX + PADDING + i * tabW;
                int tw = tabW - 2;
                if (mouseX >= tx && mouseX < tx + tw && mouseY >= tabY && mouseY < tabY + CATEGORY_TAB_HEIGHT) {
                    selectedCategory = cats[i];
                    scrollOffset = 0;
                    targetScroll = 0;
                    updateModuleButtons();
                    return true;
                }
            }
            for (ModuleButton btn : moduleButtons) {
                if (btn.isHovered((int) mouseX, (int) mouseY, (int) scrollOffset)) {
                    btn.getModule().toggle();
                    AlpineVisualsClient.getInstance().getConfigManager().save(
                        AlpineVisualsClient.getInstance().getVisualManager());
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        targetScroll += (float)(amount * 30);
        int panelY = (height - PANEL_HEIGHT) / 2;
        int contentH = PANEL_HEIGHT - CATEGORY_TAB_HEIGHT - 52;
        int totalH = moduleButtons.size() * (MODULE_HEIGHT + 4);
        float minS = -(totalH - contentH);
        if (targetScroll > 0) targetScroll = 0;
        if (totalH > contentH && targetScroll < minS) targetScroll = minS;
        if (totalH <= contentH) targetScroll = 0;
        return true;
    }

    @Override
    public boolean shouldPause() { return false; }

    @Override
    public void close() {
        AlpineVisualsClient.getInstance().getConfigManager().save(
            AlpineVisualsClient.getInstance().getVisualManager());
        super.close();
    }
}
