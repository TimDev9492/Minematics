package me.timwastaken.minematics.models;

import me.timwastaken.minematics.models.components.BlockComponent;
import me.timwastaken.minematics.models.components.RelativePositionComponent;
import me.timwastaken.minematics.models.template.MinematicComponent;
import me.timwastaken.minematics.models.template.MinematicComposite;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class MinematicChain extends MinematicComposite {
    private final float chainLength;

    public MinematicChain(Material mat, Location basePos, float segmentLength, int numSegments, float baseWidth, float topWidth) {
        super();
        this.chainLength = segmentLength * numSegments;

        BlockComponent parent = null;
        for (int i = 0; i < numSegments; i++) {
            float currentWidth = baseWidth + (topWidth - baseWidth) * (i / (float) (numSegments-1));
            Location loc;
            if (parent == null) {
                // absolute location
                loc = basePos.clone();
            } else {
                // relative location
                loc = parent.getDirection().clone().multiply(parent.getDimensions().getZ()).toLocation(parent.getPosition().getWorld());
            }
            BlockComponent segment = MinematicFactory.createRelativeBlockEntity(
                    parent,
                    mat,
                    loc,
                    new Vector(currentWidth, currentWidth, segmentLength),
                    new Vector(-currentWidth / 2, -currentWidth / 2, 0),
                    new Vector(0, 1, 0)
            );

            this.addChild(segment);

            parent = segment;
        }
    }

    private BlockComponent getBase() {
        return (BlockComponent) this.getChildren().getFirst();
    }

    private BlockComponent getEnd() {
        return (BlockComponent) this.getChildren().getLast();
    }
}
