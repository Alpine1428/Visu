package com.alpine.visuals.client.visual;

import com.alpine.visuals.client.visual.modules.*;
import net.minecraft.client.MinecraftClient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VisualManager {
    private final List<VisualModule> modules = new ArrayList<>();

    public VisualManager() {
        modules.add(new FullbrightModule());
        modules.add(new CustomCrosshairModule());
        modules.add(new BlockHighlightModule());
        modules.add(new NoFogModule());
        modules.add(new ItemPhysicsModule());
        modules.add(new HudInfoModule());
        modules.add(new ArmorHudModule());
        modules.add(new CoordinatesModule());
        modules.add(new CompassModule());
        modules.add(new PotionTimerModule());
        modules.add(new ChunkBordersModule());
        modules.add(new TimeChangerModule());
        modules.add(new ParticleTrailModule());
        modules.add(new BeaconBeamModule());
        modules.add(new ZoomModule());
        modules.add(new ScreenshotHelperModule());
        modules.add(new AnimationsModule());
    }

    public List<VisualModule> getModules() {
        return modules;
    }

    public List<VisualModule> getModulesByCategory(VisualModule.Category category) {
        return modules.stream()
            .filter(m -> m.getCategory() == category)
            .collect(Collectors.toList());
    }

    public VisualModule getModule(String name) {
        return modules.stream()
            .filter(m -> m.getName().equalsIgnoreCase(name))
            .findFirst().orElse(null);
    }

    public void tick(MinecraftClient client) {
        for (VisualModule module : modules) {
            if (module.isEnabled()) {
                module.onTick(client);
            }
        }
    }
}
