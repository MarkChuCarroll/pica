package org.goodmath.pica.ast.actions;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.Twist;

import java.util.List;

public class SpawnAction extends Action {
    private final Action action;

    public SpawnAction(Action action, Location loc) {
        super(loc);
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public Twist twist() {
        return Twist.obj("Action::Spawn",
                action);
    }
}
