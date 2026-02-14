package com.alpine.visuals.mixin;

import com.alpine.visuals.client.visual.modules.TimeChangerModule;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow
    private ClientWorld world;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(MatrixStack matrices, float tickDelta, long limitTime,
                          boolean renderBlockOutline, Camera camera,
                          GameRenderer gameRenderer,
                          LightmapTextureManager lightmapTextureManager,
                          Matrix4f projectionMatrix, CallbackInfo ci) {
        if (TimeChangerModule.active && world != null) {
            world.setTimeOfDay(TimeChangerModule.clientTime);
        }
    }
}
