package org.goodmath.pica.ast.types;

import java.util.List;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.util.TagTree;


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
    public TagTree getTree() {
        return new TagTree("Type::Function",
            List.of(new TagTree("params",
                getArgs().stream().map(Type::getTree).toList()),
                returnType.getTree()));
    }

}
