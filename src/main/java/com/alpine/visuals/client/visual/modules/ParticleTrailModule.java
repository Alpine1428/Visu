package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleTypes;

public class ParticleTrailModule extends VisualModule {
    public static boolean active = false;
    private int tickCounter = 0;

    public ParticleTrailModule() {
        super("Particle Trail", "Leave beautiful particles behind you", Category.WORLD);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        tickCounter++;
        if (tickCounter % 2 == 0) {
            double x = client.player.getX();
            double y = client.player.getY() + 0.1;
            double z = client.player.getZ();
            client.world.addParticle(
                ParticleTypes.END_ROD,
                x + (Math.random() - 0.5) * 0.5,
                y + Math.random() * 0.3,
                z + (Math.random() - 0.5) * 0.5,
                0, 0.02, 0
            );
        }
    }
}
