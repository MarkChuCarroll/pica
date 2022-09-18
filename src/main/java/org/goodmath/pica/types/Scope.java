package org.goodmath.pica.types;

import org.goodmath.pica.ast.Definition;
import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.ast.types.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.goodmath.pica.ast.locations.Location.Unlocated;

public abstract class Scope {
    protected Map<String, Defined> definitions = new HashMap<String, Defined>();
    protected Map<String, Type> types = new HashMap<String, Type>();

    abstract Optional<Defined> getDefinition(Identifier id);

    abstract Optional<Type> getType(Identifier id);


    public void setDefinition(String name, Defined def) {
        definitions.put(name, def);
    }

    public void setType(String name, Type type) {
        types.put(name, type);
    }

    public static RootScopeType RootScope = new RootScopeType();

    public static class RootScopeType extends Scope {
        RootScopeType() {

        }
        public Identifier rootId = new Identifier(Optional.empty(), "RootScope", Unlocated);
        Map<Identifier, ModuleScope> modules = new HashMap<Identifier, ModuleScope>();


        @Override
        Optional<Defined> getDefinition(Identifier id) {
            // TODO();
            return Optional.empty();
        }

        @Override
        Optional<Type> getType(Identifier id) {
            return Optional.empty();
        }

        public Optional<ModuleScope> getModule(Identifier id) {
            return Optional.ofNullable(modules.get(id));
        }

        public void setModule(Identifier id, ModuleScope scope) {
            modules.put(id,scope);
        }

        public boolean includesModule(Identifier id) {
            return modules.containsKey(id);
        }
    };

}
