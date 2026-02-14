package com.alpine.visuals.client.visual;

import com.alpine.visuals.client.visual.modules.*;
import net.minecraft.client.MinecraftClient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VisualManager {
    private final List<VisualModule> modules = new ArrayList<>();

    public VisualManager() {
        // RENDER
        modules.add(new FullbrightModule());
        modules.add(new CustomCrosshairModule());
        modules.add(new NoFogModule());
        modules.add(new BlockHighlightModule());
        modules.add(new ItemPhysicsModule());
        modules.add(new GlowESPModule());
        modules.add(new NameTagsModule());
        modules.add(new TrailRendererModule());
        modules.add(new ViewBobbingModule());
        modules.add(new HandSwingModule());

        // HUD
        modules.add(new HudInfoModule());
        modules.add(new ArmorHudModule());
        modules.add(new CoordinatesModule());
        modules.add(new CompassModule());
        modules.add(new PotionTimerModule());
        modules.add(new KeystrokesModule());
        modules.add(new SpeedDisplayModule());
        modules.add(new ClockModule());
        modules.add(new CPSModule());
        modules.add(new TargetHudModule());

        // WORLD
        modules.add(new ChunkBordersModule());
        modules.add(new TimeChangerModule());
        modules.add(new ParticleTrailModule());
        modules.add(new BeaconBeamModule());
        modules.add(new CustomSkyModule());
        modules.add(new AmbientParticlesModule());
        modules.add(new WaypointsModule());

        // PLAYER
        modules.add(new ZoomModule());
        modules.add(new FreelookModule());
        modules.add(new NoBobModule());
        modules.add(new NoHurtCamModule());
        modules.add(new SpinBotVisualModule());
        modules.add(new CapeModule());

        // MISC
        modules.add(new AnimationsModule());
        modules.add(new ScreenEffectsModule());
        modules.add(new TNTTimerModule());
        modules.add(new ItemModelModule());
    }

    public List<VisualModule> getModules() { return modules; }
    public List<VisualModule> getModulesByCategory(VisualModule.Category c) {
        return modules.stream().filter(m -> m.getCategory() == c).collect(Collectors.toList());
    }
    public VisualModule getModule(String n) {
        return modules.stream().filter(m -> m.getName().equalsIgnoreCase(n)).findFirst().orElse(null);
    }
    public void tick(MinecraftClient cl) {
        for (VisualModule m : modules) { if (m.isEnabled()) m.onTick(cl); }
    }
}
