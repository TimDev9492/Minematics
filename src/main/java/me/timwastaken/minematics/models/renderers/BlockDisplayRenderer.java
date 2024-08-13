package me.timwastaken.minematics.models.renderers;

import me.timwastaken.minematics.models.components.ArrowComponent;
import me.timwastaken.minematics.models.template.MinematicComposite;
import me.timwastaken.minematics.models.template.MinematicEntity;
import me.timwastaken.minematics.models.components.BlockComponent;
import me.timwastaken.minematics.models.template.MinematicComponent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BlockDisplayRenderer implements IMinematicEntityRenderer {
    private static final Vector3f UP_VECTOR = new Vector3f(0, 1, 0);

    private final HashMap<MinematicComponent, List<BlockDisplay>> blockDisplays;

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
            } else if (entity instanceof ArrowComponent arrowComponent) {
                this.renderArrowComponent(arrowComponent);
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
        blockDisplay.teleport(component.getPosition());
    }

    public void renderArrowComponent(BlockComponent component) {
        float arrowStrength = 0.05f;
        float tipLength = 0.2f;

        List<BlockDisplay> arrowDisplays = this.getOrCreateBlockDisplays(component);
        BlockDisplay arrowBody = arrowDisplays.get(0);
        BlockDisplay arrowTipUp = arrowDisplays.get(1);
        BlockDisplay arrowTipDown = arrowDisplays.get(2);
        arrowBody.setBlock(component.getBlockData());
        arrowTipUp.setBlock(component.getBlockData());
        arrowTipDown.setBlock(component.getBlockData());

        arrowBody.setTransformation(this.getTransformation(
                component.getDirection().clone().normalize(),
                new Vector(-arrowStrength / 2, -arrowStrength / 2, 0),
                new Vector(arrowStrength, arrowStrength, component.getDirection().distance(new Vector(0, 0, 0)))
        ));
        arrowBody.teleport(component.getPosition());

        Location arrowTip = component.getPosition().clone().add(component.getDirection());
        Vector cross = component.getDirection().getCrossProduct(new Vector(0, 1, 0)).normalize();
        Vector normalizedToRotate = component.getDirection().clone().multiply(-1).normalize();
        Vector upTipDir = normalizedToRotate.clone().rotateAroundAxis(cross, Math.toRadians(45));
        Vector downTipDir = normalizedToRotate.clone().rotateAroundAxis(cross, Math.toRadians(-45));

        arrowTipUp.setTransformation(this.getTransformation(
                upTipDir,
                new Vector(-arrowStrength / 2, -arrowStrength / 2, 0),
                new Vector(arrowStrength, arrowStrength, tipLength)
        ));
        arrowTipDown.setTransformation(this.getTransformation(
                downTipDir,
                new Vector(-arrowStrength / 2, -arrowStrength / 2, 0),
                new Vector(arrowStrength, arrowStrength, tipLength)
        ));
        arrowTipUp.teleport(arrowTip);
        arrowTipDown.teleport(arrowTip);
    }

    public void renderArrowComponent(ArrowComponent component) {
        float arrowStrength = 0.05f;
        float tipLength = 0.2f;

        List<BlockDisplay> arrowDisplays = this.getOrCreateBlockDisplays(component);
        BlockDisplay arrowBody = arrowDisplays.get(0);
        BlockDisplay arrowTipUp = arrowDisplays.get(1);
        BlockDisplay arrowTipDown = arrowDisplays.get(2);
        arrowBody.setBlock(component.getBlockData());
        arrowTipUp.setBlock(component.getBlockData());
        arrowTipDown.setBlock(component.getBlockData());

        arrowBody.setTransformation(this.getTransformation(
                component.getDirection().clone().normalize(),
                new Vector(-arrowStrength / 2, -arrowStrength / 2, 0),
                new Vector(arrowStrength, arrowStrength, component.getDirection().distance(new Vector(0, 0, 0)))
        ));
        arrowBody.teleport(component.getPosition());

        Location arrowTip = component.getPosition().clone().add(component.getDirection());
        Vector cross = component.getDirection().getCrossProduct(new Vector(0, 1, 0)).normalize();
        Vector normalizedToRotate = component.getDirection().clone().multiply(-1).normalize();
        Vector upTipDir = normalizedToRotate.clone().rotateAroundAxis(cross, Math.toRadians(45));
        Vector downTipDir = normalizedToRotate.clone().rotateAroundAxis(cross, Math.toRadians(-45));

        arrowTipUp.setTransformation(this.getTransformation(
                upTipDir,
                new Vector(-arrowStrength / 2, -arrowStrength / 2, 0),
                new Vector(arrowStrength, arrowStrength, tipLength)
        ));
        arrowTipDown.setTransformation(this.getTransformation(
                downTipDir,
                new Vector(-arrowStrength / 2, -arrowStrength / 2, 0),
                new Vector(arrowStrength, arrowStrength, tipLength)
        ));
        arrowTipUp.teleport(arrowTip);
        arrowTipDown.teleport(arrowTip);
    }

    private List<BlockDisplay> getOrCreateBlockDisplays(BlockComponent component) {
        if (this.blockDisplays.containsKey(component))
            return this.blockDisplays.get(component);
        World world = Objects.requireNonNull(component.getPosition().getWorld());
        BlockDisplay arrowBody = (BlockDisplay) world.spawnEntity(
                component.getPosition(),
                EntityType.BLOCK_DISPLAY
        );
        BlockDisplay arrowTipUp = (BlockDisplay) world.spawnEntity(
                component.getPosition(),
                EntityType.BLOCK_DISPLAY
        );
        BlockDisplay arrowTipDown = (BlockDisplay) world.spawnEntity(
                component.getPosition(),
                EntityType.BLOCK_DISPLAY
        );
        List<BlockDisplay> arrow = List.of(arrowBody, arrowTipUp, arrowTipDown);
        this.blockDisplays.put(component, arrow);
        return arrow;
    }

    private List<BlockDisplay> getOrCreateBlockDisplays(ArrowComponent component) {
        if (this.blockDisplays.containsKey(component))
            return this.blockDisplays.get(component);
        World world = Objects.requireNonNull(component.getPosition().getWorld());
        BlockDisplay arrowBody = (BlockDisplay) world.spawnEntity(
                component.getPosition(),
                EntityType.BLOCK_DISPLAY
        );
        BlockDisplay arrowTipUp = (BlockDisplay) world.spawnEntity(
                component.getPosition(),
                EntityType.BLOCK_DISPLAY
        );
        BlockDisplay arrowTipDown = (BlockDisplay) world.spawnEntity(
                component.getPosition(),
                EntityType.BLOCK_DISPLAY
        );
        List<BlockDisplay> arrow = List.of(arrowBody, arrowTipUp, arrowTipDown);
        this.blockDisplays.put(component, arrow);
        return arrow;
    }

    private BlockDisplay getOrCreateBlockDisplay(BlockComponent component) {
        if (this.blockDisplays.containsKey(component))
            return this.blockDisplays.get(component).getFirst();
        World world = Objects.requireNonNull(component.getPosition().getWorld());
        BlockDisplay blockDisplay = (BlockDisplay) world.spawnEntity(
                component.getPosition(),
                EntityType.BLOCK_DISPLAY
        );
        // smooth transitions
        blockDisplay.setInterpolationDelay(5);
        blockDisplay.setTeleportDuration(5);
        this.blockDisplays.put(component, List.of(blockDisplay));
        return blockDisplay;
    }

    private List<BlockDisplay> removeBlockDisplays(MinematicComponent component) {
        List<BlockDisplay> blockDisplays = this.blockDisplays.remove(component);
        if (blockDisplays != null)
            blockDisplays.forEach(BlockDisplay::remove);
        return blockDisplays;
    }

    private Transformation getTransformation(Vector direction, Vector translation, Vector scale) {
        Matrix4f transformationMatrix = new Matrix4f()
                .rotateTowards(direction.toVector3f(),
                        direction.getX() + direction.getZ() != 0 ? UP_VECTOR : new Vector3f(0, 0, 1))
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
        this.blockDisplays.keySet().forEach(this::removeBlockDisplays);
    }
}
