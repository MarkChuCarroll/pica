package org.goodmath.pica.ast.actions;


import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.Twist;


public class ExitAction extends Action {

    public ExitAction(Location loc) {
        super(loc);
    }

    @Override
    public Twist twist() {
        return Twist.obj("Action::Exit");
    }
}
