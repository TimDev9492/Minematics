package me.timwastaken.minematics.models;

import me.timwastaken.minematics.common.exceptions.CannotReachException;
import me.timwastaken.minematics.models.components.BlockComponent;
import me.timwastaken.minematics.models.template.MinematicEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class MinematicFabrikChain extends MinematicChain {
    private static final double FABRIK_TOLERANCE = 1e-2;

    public MinematicFabrikChain(Material mat, Location basePos, float segmentLength, int numSegments, float baseWidth, float topWidth) {
        super(mat, basePos, segmentLength, numSegments, baseWidth, topWidth);
    }

    public void reachTo(Location target) throws CannotReachException {
        if (!this.getBase().getPosition().getWorld().equals(target.getWorld())
        || this.getBase().getPosition().distanceSquared(target) > this.chainLength * this.chainLength) {
            throw new CannotReachException("Target is too far away!");
        }
        this.straightenTowards(target);
        this.fabrik(target);
    }

    /**
     * Straighten the chain towards a target.
     * @param target the target to straighten towards
     */
    public void straightenTowards(Location target) {
        Vector horizontalDirection = target.clone()
                .subtract(this.getBase().getPosition())
                .toVector();
        horizontalDirection.setY(0).normalize();

        double horizBaseDirComponent = this.getBase().getDirection().clone()
                .setY(0).distance(new Vector(0, 0, 0));

        Vector newDirection = new Vector(0, this.getBase().getDirection().getY(), 0)
                .add(horizontalDirection.multiply(horizBaseDirComponent));

        this.getBase().setDirection(newDirection.clone());
        for (int i = 1; i < this.getChildren().size(); i++) {
            BlockComponent segment = (BlockComponent) this.getChildren().get(i);
            BlockComponent parent = (BlockComponent) this.getChildren().get(i-1);

            Location parentTip = parent.getPosition().clone()
                    .add(parent.getDirection().clone()
                            .multiply(parent.getDimensions().getZ()));
            segment.setPosition(parentTip);
            segment.setDirection(newDirection.clone());
        }
    }

    /**
     * Moves the end effector of the chain to the target
     * position using the FABRIK algorithm.
     * Does not check if target is reachable prior to execution!
     * @param target the target position to reach
     */
    public void fabrik(Location target) {
        Location basePos = this.getBase().getPosition().clone();

        int iterations = 0;
        while (this.getEndEffector().distanceSquared(target) > FABRIK_TOLERANCE * FABRIK_TOLERANCE) {
            iterations++;
            // propagate from end to base
            Location currentTarget = target.clone();
            for (int i = this.getChildren().size() - 1; i >= 0; i--) {
                BlockComponent segment = (BlockComponent) this.getChildren().get(i);
                Vector newDirection = currentTarget.clone()
                        .subtract(segment.getPosition())
                        .toVector().normalize();
                Vector newPointingVector = newDirection.clone()
                        .multiply(segment.getDimensions().getZ());
                currentTarget.subtract(newPointingVector);
                segment.setDirection(newDirection);
                segment.setPosition(currentTarget.clone());
            }
            // propagate from base to end to ensure base has not moved
            currentTarget = basePos.clone();
            for (int i = 0; i < this.getChildren().size(); i++) {
                BlockComponent segment = (BlockComponent) this.getChildren().get(i);
                Vector pointingVector = segment.getDirection().clone()
                        .multiply(segment.getDimensions().getZ());
                Vector newDirection = segment.getPosition().clone()
                        .add(pointingVector)
                        .subtract(currentTarget)
                        .toVector().normalize();
                segment.setPosition(currentTarget.clone());
                segment.setDirection(newDirection);
                currentTarget.add(newDirection.clone()
                        .multiply(segment.getDimensions().getZ()));
            }
        }
        System.out.println("FABRIK took " + iterations + " iterations.");
    }

    private FabrikJob jobState = null;

    public void initializeFabrikJob(Location target) {
        this.jobState = new FabrikJob(
                true,
                this.getChildren().size() - 1,
                this.getBase().getPosition().clone(),
                target.clone(),
                target.clone()
        );
    }

    public void stepFabrik() {
        if (this.jobState == null) return;
        BlockComponent segment = (BlockComponent) this.getChildren().get(this.jobState.index());
        // want to compute
        Location newSegmentPos;
        Vector newSegmentDir;
        Location nextTarget;
        boolean nextReverse = this.jobState.reverse();
        int nextIndex;

        if (this.jobState.reverse()) {
            // backwards propagation step
            newSegmentDir = this.jobState.currentTarget().clone()
                    .subtract(segment.getPosition())
                    .toVector().normalize();
            Vector newPointingVector = newSegmentDir.clone()
                    .multiply(segment.getDimensions().getZ());
            nextTarget = this.jobState.currentTarget().clone().subtract(newPointingVector);
            newSegmentPos = nextTarget.clone();
            if (this.jobState.index() > 0) nextIndex = this.jobState.index() - 1;
            else {
                nextIndex = 0;
                nextReverse = false;
                nextTarget = this.jobState.base().clone();
            }
        } else {
            // forwards propagation step
            Vector segmentPointingVector = segment.getDirection().clone()
                    .multiply(segment.getDimensions().getZ());
            newSegmentDir = segment.getPosition().clone()
                    .add(segmentPointingVector)
                    .subtract(this.jobState.currentTarget())
                    .toVector().normalize();
            newSegmentPos = this.jobState.currentTarget().clone();
            nextTarget = newSegmentPos.clone()
                    .add(newSegmentDir.clone()
                    .multiply(segment.getDimensions().getZ()));
            if (this.jobState.index() < this.getChildren().size() - 1) nextIndex = this.jobState.index() + 1;
            else {
                nextIndex = this.getChildren().size() - 1;
                nextReverse = true;
                nextTarget = this.jobState.target().clone();
            }
        }

        // set new values
        segment.setDirection(newSegmentDir);
        segment.setPosition(newSegmentPos);
        // save state for next step
        this.jobState = new FabrikJob(
                nextReverse,
                nextIndex,
                this.jobState.base(),
                this.jobState.target(),
                nextTarget
        );
    }
}

record FabrikJob(boolean reverse, int index, Location base, Location target, Location currentTarget) {

}
