package me.timwastaken.minematics.engine;

import me.timwastaken.minematics.models.template.MinematicEntity;
import me.timwastaken.minematics.models.renderers.IMinematicEntityRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a singleton engine that updates and renders MinematicEntities.
 */
public class MinematicEngine {
    private static MinematicEngine singletonInstance = null;

    private MinematicEngine() {
        this.trackedEntities = new ArrayList<>();
    }

    public static MinematicEngine getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new MinematicEngine();
        }
        return singletonInstance;
    }

    private final List<MinematicEntity> trackedEntities;

    public void tick() {

    }

    public void render(IMinematicEntityRenderer renderer) {
        for (MinematicEntity entity : trackedEntities) {
            renderer.render(entity);
        }
    }

    public void trackEntity(MinematicEntity entity) {
        trackedEntities.add(entity);
    }

    public void untrackEntity(MinematicEntity entity) {
        trackedEntities.remove(entity);
    }
}
