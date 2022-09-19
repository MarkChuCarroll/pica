package org.goodmath.pica.ast.actions;

import java.util.Collections;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.TagTree;


public class ExitAction extends Action {

    public ExitAction(Location loc) {
        super(loc);
    }

    @Override
    public TagTree getTree() {
        return new TagTree("Action::Exit", Collections.emptyList());
    }
}
