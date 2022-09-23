package org.goodmath.pica.types;

import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.ast.PicaModule;
import org.goodmath.pica.ast.types.Type;

import java.util.*;

import static org.goodmath.pica.ast.locations.Location.NO_LOCATION;

public class RootScope extends Scope {
    public static RootScope root = new RootScope();

    RootScope() {

    }
    public Identifier rootId = new Identifier(Optional.empty(), "RootScope", NO_LOCATION);
    Map<Identifier, ModuleScope> modules = new HashMap<>();


    @Override
    public Optional<Defined> getDefinition(Identifier id) {
        // TODO();
        return Optional.empty();
    }

    @Override
    public Optional<Type> getType(Identifier id) {
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

    public List<PicaModule> getModules() {
        List<PicaModule> result = new ArrayList<>();
        for (ModuleScope s: modules.values()) {
            result.add(s.getModule());
        }
        return result;
    }
}
