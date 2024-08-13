package me.timwastaken.minematics.models.template;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public abstract class MinematicComponent extends MinematicEntity {
    private Location position;
    private Vector direction;
    private Vector translation;

    public MinematicComponent() {}

    public Location getPosition() {
        return position;
    }

    public void setPosition(Location position) {
        this.position = position;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public Vector getTranslation() {
        return translation;
    }

    public void setTranslation(Vector translation) {
        this.translation = translation;
    }
}
