package com.alpine.visuals.client.gui.components;

import com.alpine.visuals.client.render.RenderUtils;
import com.alpine.visuals.client.visual.VisualModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class ModuleButton {
    private final VisualModule module;
    private final int x, baseY, width, height;
    private final int index;
    private float hoverAnim = 0;
    private float enableAnim = 0;

    public ModuleButton(VisualModule module, int x, int y, int w, int h, int idx) {
        this.module = module;
        this.x = x;
        this.baseY = y;
        this.width = w;
        this.height = h;
        this.index = idx;
    }

    public void render(DrawContext ctx, int mx, int my, float delta, int scrollOff, int gAlpha) {
        MinecraftClient client = MinecraftClient.getInstance();
        int y = baseY + scrollOff;
        boolean hov = mx >= x && mx < x + width && my >= y && my < y + height;
        boolean en = module.isEnabled();

        hoverAnim += ((hov ? 1.0f : 0.0f) - hoverAnim) * 0.3f;
        enableAnim += ((en ? 1.0f : 0.0f) - enableAnim) * 0.2f;

        int catColor = module.getCategory().color;

        int bgA = (int)((0.08f + hoverAnim * 0.06f) * gAlpha);
        int bgC = en ?
            RenderUtils.withAlpha(catColor & 0x00FFFFFF, bgA) :
            RenderUtils.withAlpha(0xFFFFFF, bgA);
        RenderUtils.drawRoundedRect(ctx, x, y, width, height, 4, bgC);

        int barH = (int)(height * enableAnim * 0.7f);
        int barY = y + (height - barH) / 2;
        if (barH > 0) {
            int barC = RenderUtils.withAlpha(catColor & 0x00FFFFFF, (int)(enableAnim * gAlpha));
            ctx.fill(x + 2, barY, x + 5, barY + barH, barC);
        }

        if (hoverAnim > 0.01f) {
            int glA = (int)(hoverAnim * 30);
            RenderUtils.drawOutline(ctx, x, y, width, height,
                RenderUtils.withAlpha(catColor & 0x00FFFFFF, glA));
        }

        int nc = en ?
            RenderUtils.withAlpha(0xFFFFFF, gAlpha) :
            RenderUtils.withAlpha(0xAAAAAA, gAlpha);
        ctx.drawText(client.textRenderer, module.getName(), x + 12, y + 6, nc, en);

        int dc = RenderUtils.withAlpha(0x777777, (int)(gAlpha * 0.8f));
        ctx.drawText(client.textRenderer, module.getDescription(), x + 12, y + 18, dc, false);

        int togX = x + width - 36;
        int togY = y + (height - 14) / 2;
        drawToggle(ctx, togX, togY, en, enableAnim, catColor, gAlpha);

        ctx.fill(x + 8, y + height - 1, x + width - 8, y + height,
            RenderUtils.withAlpha(0xFFFFFF, (int)(gAlpha * 0.04f)));
    }

    private void drawToggle(DrawContext ctx, int x, int y, boolean en, float anim, int accent, int alpha) {
        int tw = 28, th = 14;
        int trackC = RenderUtils.interpolateColor(
            RenderUtils.withAlpha(0x333333, alpha),
            RenderUtils.withAlpha(accent & 0x00FFFFFF, (int)(alpha * 0.5f)),
            anim
        );
        RenderUtils.drawRoundedRect(ctx, x, y, tw, th, 7, trackC);
        int ks = 10;
        int kx = x + 2 + (int)((tw - ks - 4) * anim);
        int kc = RenderUtils.interpolateColor(
            RenderUtils.withAlpha(0x888888, alpha),
            RenderUtils.withAlpha(0xFFFFFF, alpha),
            anim
        );
        RenderUtils.drawRoundedRect(ctx, kx, y + 2, ks, ks, 5, kc);
        if (anim > 0.5f) {
            int ga = (int)((anim - 0.5f) * 2 * 40);
            RenderUtils.drawRoundedRect(ctx, kx - 1, y + 1, ks + 2, ks + 2, 6,
                RenderUtils.withAlpha(accent & 0x00FFFFFF, ga));
        }
    }

    public boolean isHovered(int mx, int my, int scrollOff) {
        int y = baseY + scrollOff;
        return mx >= x && mx < x + width && my >= y && my < y + height;
    }

    public VisualModule getModule() { return module; }
    public int getBaseY() { return baseY; }
}
