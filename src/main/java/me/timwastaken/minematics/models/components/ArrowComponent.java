package me.timwastaken.minematics.models.components;

import me.timwastaken.minematics.models.template.MinematicComponent;
import org.bukkit.block.data.BlockData;

public class ArrowComponent extends MinematicComponentDecorator {
    private BlockData blockData;

    public ArrowComponent() {
        super();
    }
    public ArrowComponent(MinematicComponent component) {
        super(component);
    }

    public BlockData getBlockData() {
        return blockData;
    }

    public void setBlockData(BlockData blockData) {
        this.blockData = blockData;
    }
}
