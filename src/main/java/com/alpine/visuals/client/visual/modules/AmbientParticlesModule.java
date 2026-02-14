package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleTypes;

public class AmbientParticlesModule extends VisualModule {
    public static boolean active = false;
    public final Setting density;
    private int tick = 0;

    public AmbientParticlesModule() {
        super("Ambient Particles", "Floating particles around you like fireflies", Category.WORLD);
        density = addSetting("Density", 5, 1, 20);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        tick++;
        if (tick % 3 != 0) return;
        double px = client.player.getX();
        double py = client.player.getY() + 1;
        double pz = client.player.getZ();
        for (int i = 0; i < (int) density.value; i++) {
            double ox = (Math.random() - 0.5) * 16;
            double oy = (Math.random() - 0.5) * 8;
            double oz = (Math.random() - 0.5) * 16;
            client.world.addParticle(ParticleTypes.END_ROD,
                px + ox, py + oy, pz + oz,
                (Math.random() - 0.5) * 0.01, 0.01, (Math.random() - 0.5) * 0.01);
        }
    }
}
