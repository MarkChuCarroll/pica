package org.goodmath.pica.ast.types;

import java.util.List;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.PPTagNode;
import org.goodmath.pica.util.PrettyPrintTree;


public class FunType extends Type {
    private final List<Type> args;
    public List<Type> getArgs() {
        return args;
    }

    private final Type returnType;

    public Type getReturnType() {
        return returnType;
    }

    public FunType(List<Type> args, Type returnType, Location loc) {
        super(loc);
        this.args = args;
        this.returnType = returnType;
    }

    @Override
    public PrettyPrintTree getTree() {
        return new PPTagNode("Type::Function",
            List.of(new PPTagNode("params",
                getArgs().stream().map(Type::getTree).toList()),
                getReturnType().getTree()));
    }

}
