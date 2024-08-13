package me.timwastaken.minematics.models.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MinematicComposite extends MinematicEntity {
    private final List<MinematicEntity> children;

    public MinematicComposite() {
        this.children = new ArrayList<>();
    }
    public MinematicComposite(Collection<MinematicEntity> children) {
        this.children = new ArrayList<>(children);
    }
    public MinematicComposite(MinematicEntity... children) {
        this.children = new ArrayList<>(Arrays.asList(children));
    }

    public void addChild(MinematicEntity child) {
        children.add(child);
    }

    public boolean removeChild(MinematicEntity child) {
        return children.remove(child);
    }

    public List<MinematicEntity> getChildren() {
        return children;
    }
}
