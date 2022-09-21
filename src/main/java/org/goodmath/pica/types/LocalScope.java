package org.goodmath.pica.types;

import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.ast.types.Type;

import java.util.Optional;

public class LocalScope extends Scope {
    private final Scope parent;

    public LocalScope(Scope parent) {
        this.parent = parent;
    }

    @Override
    public Optional<Defined> getDefinition(Identifier id) {
        if (id.getModule().isEmpty() && definitions.containsKey(id.getName())) {
            return Optional.of(definitions.get(id.getName()));
        } else {
            return parent.getDefinition(id);
        }
    }

    @Override
    public Optional<Type> getType(Identifier id) {
        if (id.getModule().isEmpty() && types.containsKey(id.getName())) {
            return Optional.of(types.get(id.getName()));
        } else {
            return parent.getType(id);
        }
    }
}
