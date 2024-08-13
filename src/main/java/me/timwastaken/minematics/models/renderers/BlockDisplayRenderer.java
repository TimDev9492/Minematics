package me.timwastaken.minematics.models.renderers;

import me.timwastaken.minematics.models.template.MinematicComposite;
import me.timwastaken.minematics.models.template.MinematicEntity;
import me.timwastaken.minematics.models.components.BlockComponent;
import me.timwastaken.minematics.models.template.MinematicComponent;
import org.bukkit.World;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Objects;

public class BlockDisplayRenderer implements IMinematicEntityRenderer {
    private static final Vector3f UP_VECTOR = new Vector3f(0, 0, 1);

    private final HashMap<BlockComponent, BlockDisplay> blockDisplays;

    public BlockDisplayRenderer() {
        this.blockDisplays = new HashMap<>();
    }

    @Override
    public void render(MinematicEntity entity) {
        // traverse the composite structure of the entity recursively
        // TODO: implement iterative traversal
        if (entity instanceof MinematicComposite composite) {
            for (MinematicEntity child : composite.getChildren()) {
                this.render(child);
            }
        } else if (entity instanceof MinematicComponent) {
            // handle rendering components
            if (entity instanceof BlockComponent blockComponent) {
                this.renderBlockComponent(blockComponent);
            } else {
                throw new UnsupportedOperationException("Not yet implemented!");
            }
        } else {
            throw new IllegalArgumentException(
                    "MinematicEntity can only be MinematicComponent or MinematicComposite!"
            );
        }
    }

    public void renderBlockComponent(BlockComponent component) {
        BlockDisplay blockDisplay = this.getOrCreateBlockDisplay(component);
        blockDisplay.setBlock(component.getBlockData());
        blockDisplay.setTransformation(this.getTransformation(
                component.getDirection(),
                component.getTranslation(),
                component.getDimensions()
        ));
    }

    private BlockDisplay getOrCreateBlockDisplay(BlockComponent component) {
        if (this.blockDisplays.containsKey(component))
            return this.blockDisplays.get(component);
        World world = Objects.requireNonNull(component.getPosition().getWorld());
        BlockDisplay blockDisplay = (BlockDisplay) world.spawnEntity(
                component.getPosition(),
                EntityType.BLOCK_DISPLAY
        );
        // update instantly
        blockDisplay.setInterpolationDelay(1);
        this.blockDisplays.put(component, blockDisplay);
        return blockDisplay;
    }

    private BlockDisplay removeBlockDisplay(BlockComponent component) {
        BlockDisplay blockDisplay = this.blockDisplays.remove(component);
        if (blockDisplay != null)
            blockDisplay.remove();
        return blockDisplay;
    }

    private Transformation getTransformation(Vector direction, Vector translation, Vector scale) {
        Matrix4f transformationMatrix = new Matrix4f()
                .rotateTowards(direction.toVector3f(), UP_VECTOR)
                .translate(translation.toVector3f())
                .scale(scale.toVector3f());

        return new Transformation(
                transformationMatrix.getTranslation(new Vector3f()),
                transformationMatrix.getUnnormalizedRotation(new Quaternionf()),
                transformationMatrix.getScale(new Vector3f()),
                new Quaternionf()
        );
    }

    public void cleanup() {
        // remove all block entities from the world
        this.blockDisplays.keySet().forEach(this::removeBlockDisplay);
    }
}
