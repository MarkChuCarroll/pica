package org.goodmath.pica.types;

import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.ast.PicaModule;
import org.goodmath.pica.ast.types.Type;

import java.util.List;
import java.util.Optional;

public class ModuleScope extends Scope {
    private final Identifier moduleName;
    private final Optional<ModuleScope> parent;
    private final PicaModule sourceModule;

    public ModuleScope(Identifier moduleName, Optional<ModuleScope> parent, PicaModule sm) {
        this.moduleName = moduleName;
        this.parent = parent;
        this.sourceModule = sm;
    }

    public PicaModule getModule() {
        return sourceModule;
    }

    public List<Defined> getDefinitions() {
        return definitions.values().stream().toList();
    }

    @Override
    public Optional<Defined> getDefinition(Identifier id) {
        if (id.getModule().isPresent()) {
            if (id.getModule().get().equals(moduleName)) {
                return Optional.ofNullable(definitions.get(id.getName()));
            } else {
                return RootScope.root.getDefinition(id);
            }
        } else {
            if (definitions.containsKey(id.getName())) {
                return Optional.of(definitions.get(id.getName()));
            } else if (parent.isPresent()) {
                return parent.get().getDefinition(id);
            } else {
                return RootScope.root.getDefinition(id);
            }
        }
    }

    @Override
    public Optional<Type> getType(Identifier id) {
        if (id.getModule().isPresent()) {
            if (id.getModule().get().equals(moduleName)) {
                return Optional.ofNullable(types.get(id.getName()));
            } else {
                return RootScope.root.getType(id);
            }
        } else {
            if (types.containsKey(id.getName())) {
                return Optional.of(types.get(id.getName()));
            } else if (parent.isPresent()) {
                return parent.get().getType(id);
            } else {
                return RootScope.root.getType(id);
            }
        }
    }
}

