package org.goodmath.pica.ast.actions;

import java.util.List;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.TagTree;

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
    public TagTree getTree() {
        return new TagTree("Action::Loop",
            List.of(getBody().getTree()));
    }

}
