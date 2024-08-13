package me.timwastaken.minematics.models.components;

import me.timwastaken.minematics.models.template.GenericMinematicComponent;
import me.timwastaken.minematics.models.template.MinematicComponent;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

public class BlockComponent extends MinematicComponentDecorator {
    private BlockData blockData;
    private Vector dimensions;

    public BlockComponent() {
        super(new GenericMinematicComponent());
    }
    public BlockComponent(MinematicComponent comp) {
        super(comp);
    }

    public Vector getDimensions() {
        return dimensions;
    }

    public void setDimensions(Vector dimensions) {
        this.dimensions = dimensions;
    }

    public BlockData getBlockData() {
        return blockData;
    }

    public void setBlockData(BlockData blockData) {
        this.blockData = blockData;
    }
}
