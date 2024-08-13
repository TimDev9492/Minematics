package me.timwastaken.minematics.engine;

import me.timwastaken.minematics.models.template.MinematicEntity;
import me.timwastaken.minematics.models.renderers.IMinematicEntityRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a singleton engine that updates and renders MinematicEntities.
 */
public class MinematicEngine {
    private static MinematicEngine singletonInstance = null;

    private MinematicEngine() {
        this.trackedEntities = Collections.synchronizedList(new ArrayList<>());
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
        this.trackedEntities.add(entity);
    }

    public void untrackEntity(MinematicEntity entity) {
        trackedEntities.remove(entity);
    }

    public void trackEntities(MinematicEntity... entities) {
        trackedEntities.addAll(List.of(entities));
    }

    public void untrackEntities(MinematicEntity... entities) {
        trackedEntities.removeAll(List.of(entities));
    }

    public void runAction(Consumer<MinematicEntity> action) {
        for (MinematicEntity entity : trackedEntities) {
            action.accept(entity);
        }
    }
}
