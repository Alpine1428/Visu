package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.DefaultParticleType;

public class ParticleTrailModule extends VisualModule {
    public static boolean active = false;
    private int tick = 0;
    public final Setting density;
    public final Setting spread;

    public ParticleTrailModule() {
        super("Particle Trail", "Leave beautiful particles behind you", Category.WORLD);
        density = addSetting("Density", 3, 1, 10);
        spread = addSetting("Spread", 0.5f, 0.1f, 2.0f);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        tick++;
        if (tick % Math.max(1, 5 - (int)density.value) != 0) return;

        double x = client.player.getX();
        double y = client.player.getY() + 0.1;
        double z = client.player.getZ();
        float sp = spread.value;

        DefaultParticleType[] particles = {
            ParticleTypes.END_ROD, ParticleTypes.CHERRY_LEAVES,
            ParticleTypes.SOUL_FIRE_FLAME, ParticleTypes.WAX_ON
        };
        DefaultParticleType particle = particles[(tick / 40) % particles.length];

        for (int i = 0; i < (int)density.value; i++) {
            client.world.addParticle(particle,
                x + (Math.random() - 0.5) * sp,
                y + Math.random() * 0.3,
                z + (Math.random() - 0.5) * sp,
                0, 0.02, 0);
        }
    }
}
