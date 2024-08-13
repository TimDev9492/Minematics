package me.timwastaken.minematics.models;

import me.timwastaken.minematics.models.components.BlockComponent;
import me.timwastaken.minematics.models.template.MinematicComposite;
import me.timwastaken.minematics.models.template.MinematicEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class MinematicChain extends MinematicComposite {
    protected final float chainLength;

    public MinematicChain(Material mat, Location basePos, float segmentLength, int numSegments, float baseWidth, float topWidth) {
        super();
        this.chainLength = segmentLength * numSegments;

        Location currentBase = basePos.clone();
        for (int i = 0; i < numSegments; i++) {
            float currentWidth = baseWidth + (topWidth - baseWidth) * (i / (float) (numSegments-1));
            Location loc;

            BlockComponent segment = MinematicFactory.createMinematicBlockEntity(
                    mat,
                    currentBase.clone(),
                    new Vector(currentWidth, currentWidth, segmentLength),
                    new Vector(-currentWidth / 2, -currentWidth / 2, 0),
                    new Vector(0, 1, 0)
            );

            this.addChild(segment);
            currentBase.add(segment.getDirection().clone().multiply(segmentLength));
        }
    }

    public BlockComponent getBase() {
        return (BlockComponent) this.getChildren().getFirst();
    }

    public BlockComponent getEnd() {
        return (BlockComponent) this.getChildren().getLast();
    }

    public Location getEndEffector() {
        return this.getEnd().getPosition().clone()
                .add(this.getEnd().getDirection().clone()
                        .multiply(this.getEnd().getDimensions().getZ()));
    }

    /**
     * Set the chains base position. Move all the
     * segments accordingly to avoid disconnecting
     * the base from the rest of the chain.
     * @param pos the new base position
     */
    public void setBasePosition(Location pos) {
        Vector delta = pos.clone().subtract(this.getBase().getPosition()).toVector();
        for (MinematicEntity child : this.getChildren()) {
            if (child instanceof BlockComponent segment) {
                segment.getPosition().add(delta);
            } else {
                throw new IllegalStateException("Children of MinematicChain must be BlockComponents!");
            }
        }
    }
}
