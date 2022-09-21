package org.goodmath.pica.types;


import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.ast.types.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class Scope {
    protected final Map<String, Defined> definitions = new HashMap<>();
    protected final Map<String, Type> types = new HashMap<>();

    public abstract Optional<Defined> getDefinition(Identifier id);

    public abstract Optional<Type> getType(Identifier id);


    public void setDefinition(String name, Defined def) {
        definitions.put(name, def);
    }

    public void setType(String name, Type type) {
        types.put(name, type);
    }

}
