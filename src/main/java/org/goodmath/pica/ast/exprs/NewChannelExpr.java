package org.goodmath.pica.ast.exprs;

import java.util.List;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.ast.types.Type;
import org.goodmath.pica.util.PPTagNode;
import org.goodmath.pica.util.PrettyPrintTree;

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
    public PrettyPrintTree getTree() {
        return new PPTagNode("Expr::NewChannel",
            List.of(getType().getTree()));
    }

}
