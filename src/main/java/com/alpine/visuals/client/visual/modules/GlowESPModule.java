package com.alpine.visuals.client.visual.modules;

import com.alpine.visuals.client.visual.VisualModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;

public class GlowESPModule extends VisualModule {
    public static boolean active = false;
    public final Setting players;
    public final Setting hostiles;
    public final Setting animals;

    public GlowESPModule() {
        super("Glow ESP", "Glowing outline on entities through walls", Category.RENDER);
        players = addSetting("Players", true);
        hostiles = addSetting("Hostiles", true);
        animals = addSetting("Animals", false);
    }
    @Override public void onEnable() { active = true; }
    @Override public void onDisable() { active = false; }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.world == null) return;
        for (Entity entity : client.world.getEntities()) {
            if (entity == client.player) continue;
            boolean shouldGlow = false;
            if (entity instanceof PlayerEntity && players.boolValue) shouldGlow = true;
            if (entity instanceof HostileEntity && hostiles.boolValue) shouldGlow = true;
            if (entity instanceof AnimalEntity && animals.boolValue) shouldGlow = true;
            entity.setGlowing(shouldGlow);
        }
    }

    @Override
    public void onDisable() {
        active = false;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null) {
            for (Entity entity : client.world.getEntities()) {
                if (entity != client.player) entity.setGlowing(false);
            }
        }
    }
}
