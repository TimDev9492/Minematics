package me.timwastaken.minematics.models.components;

import me.timwastaken.minematics.models.template.MinematicComponent;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public abstract class MinematicComponentDecorator extends MinematicComponent {
    private MinematicComponent component;

    public MinematicComponentDecorator(MinematicComponent component) {
        this.component = component;
    }

    @Override
    public Location getPosition() {
        return component.getPosition();
    }

    @Override
    public void setPosition(Location position) {
        component.setPosition(position);
    }

    @Override
    public Vector getDirection() {
        return component.getDirection();
    }

    @Override
    public void setDirection(Vector direction) {
        component.setDirection(direction);
    }

    @Override
    public Vector getTranslation() {
        return component.getTranslation();
    }

    @Override
    public void setTranslation(Vector translation) {
        component.setTranslation(translation);
    }
}
