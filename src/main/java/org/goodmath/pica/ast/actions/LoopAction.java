package org.goodmath.pica.ast.actions;

import java.util.List;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.PPTagNode;
import org.goodmath.pica.util.PrettyPrintTree;

public class LoopAction extends Action {
    private final Action body;

    public Action getBody() {
        return body;
    }

    public LoopAction(Action body, Location loc) {
        super(loc);
        this.body = body;
    }

    @Override
    public PrettyPrintTree getTree() {
        return new PPTagNode("Action::Loop",
            List.of(getBody().getTree()));
    }

}
