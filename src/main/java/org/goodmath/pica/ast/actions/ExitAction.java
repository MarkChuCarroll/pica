package org.goodmath.pica.ast.actions;


import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.PPLeafNode;
import org.goodmath.pica.util.PrettyPrintTree;


public class ExitAction extends Action {

    public ExitAction(Location loc) {
        super(loc);
    }

    @Override
    public PrettyPrintTree getTree() {
        return new PPLeafNode("Action::Exit");
    }
}
