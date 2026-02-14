package com.alpine.visuals.mixin;

import com.alpine.visuals.client.visual.modules.ZoomModule;
import com.alpine.visuals.client.visual.modules.NoHurtCamModule;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void onGetFov(Camera camera, float tickDelta, boolean changing, CallbackInfoReturnable<Double> cir) {
        if (ZoomModule.active && ZoomModule.zooming) {
            cir.setReturnValue(cir.getReturnValue() / ZoomModule.zoomFactor);
        }
    }

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void onHurtCam(net.minecraft.client.util.math.MatrixStack ms, float td, CallbackInfo ci) {
        if (NoHurtCamModule.active) ci.cancel();
    }
}
