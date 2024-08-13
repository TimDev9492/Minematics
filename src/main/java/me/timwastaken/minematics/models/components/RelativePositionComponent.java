package me.timwastaken.minematics.models.components;

import me.timwastaken.minematics.models.template.GenericMinematicComponent;
import me.timwastaken.minematics.models.template.MinematicComponent;
import org.bukkit.Location;

/**
 * Represents a component that has a relative position to another component.
 * Setting the parent to null has no effect on the component.
 */
public class RelativePositionComponent extends MinematicComponentDecorator {
    private final MinematicComponent parent;

    public RelativePositionComponent(GenericMinematicComponent self, MinematicComponent parent) {
        super(self);
        if (parent == null) throw new IllegalArgumentException("Parent cannot be null.");
        this.parent = parent;
    }

    @Override
    public Location getPosition() {
        return parent.getPosition().clone().add(super.getPosition());
    }


}
