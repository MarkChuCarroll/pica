package org.goodmath.pica.ast.actions;

import java.util.List;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.Twist;

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
    public Twist twist() {
        return Twist.obj("Action::Loop",
            getBody());
    }

}
