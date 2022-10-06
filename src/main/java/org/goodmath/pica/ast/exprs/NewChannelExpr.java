package org.goodmath.pica.ast.exprs;

import java.util.List;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.ast.types.Type;
import org.goodmath.pica.util.Twist;

public class NewChannelExpr extends Expr {
    private final Type type;

    public Type getType() {
        return type;
    }

    public NewChannelExpr(Type t, Location loc) {
        super(loc);
        this.type = t;
    }

    @Override
    public Twist twist() {
        return Twist.obj("Expr::NewChannel",
                Twist.val("type", getType()));
    }

}
