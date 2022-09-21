package org.goodmath.pica.compiler;

import org.goodmath.pica.ast.locations.Location;
import org.goodmath.pica.ast.types.Type;
import org.goodmath.pica.util.PPTagNode;
import org.goodmath.pica.util.PrettyPrintTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TypeVar extends Type {
    private static int typeArgumentIndex = 0;
    private final String name;
    private final String id;

    private Optional<Type> binding;

    public TypeVar(String name, Location loc) {
        super(loc);
        typeArgumentIndex++;
        this.name = name;
        this.id = name + "_" + typeArgumentIndex;
        this.binding = Optional.empty();
    }

    @Override
    public PrettyPrintTree getTree() {
        List<PrettyPrintTree> children = new ArrayList<>();
        children.add(new PPTagNode(getId()));
        getBinding().ifPresent(b ->
            children.add(new PPTagNode("Type::Binding", List.of(b.getTree()))));

        return new PPTagNode("Type::TypeVar",
                children);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setBinding(Type t) {
        if (getBinding().map(b -> b instanceof TypeVar).orElse(false)) {
            TypeVar b = (TypeVar)(getBinding().get());
            b.setBinding(t);
        }
        binding = Optional.of(t);
    }

    public Optional<Type> getBinding() {
        return binding;
    }
}
