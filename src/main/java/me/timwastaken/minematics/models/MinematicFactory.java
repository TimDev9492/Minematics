package me.timwastaken.minematics.models;

import me.timwastaken.minematics.models.components.BlockComponent;
import me.timwastaken.minematics.models.components.RelativePositionComponent;
import me.timwastaken.minematics.models.template.GenericMinematicComponent;
import me.timwastaken.minematics.models.template.MinematicComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class MinematicFactory {
    public static BlockComponent createMinematicBlockEntity(
            Material mat,
            Location pos,
            Vector dimensions,
            Vector translation,
            Vector direction
    ) {
        BlockComponent block = new BlockComponent();
        block.setBlockData(mat.createBlockData());
        block.setDimensions(dimensions);
        block.setPosition(pos);
        block.setTranslation(translation);
        block.setDirection(direction);
        return block;
    }

    public static BlockComponent createRelativeBlockEntity(
            MinematicComponent parent,
            Material mat,
            Location pos,
            Vector dimensions,
            Vector translation,
            Vector direction
    ) {
        GenericMinematicComponent generic = new GenericMinematicComponent();
        generic.setPosition(pos);
        generic.setTranslation(translation);
        generic.setDirection(direction);

        BlockComponent block;
        if (parent == null) {
            block = new BlockComponent(generic);
        } else {
            block = new BlockComponent(new RelativePositionComponent(generic, parent));
        }
        block.setBlockData(mat.createBlockData());
        block.setDimensions(dimensions);

        return block;
    }
}
