package org.goodmath.pica.ast.actions;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.PPTagNode;
import org.goodmath.pica.util.PrettyPrintTree;

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
    public PrettyPrintTree getTree() {
        return new PPTagNode("Action::Spawn",
                List.of(action.getTree()));
    }
}
