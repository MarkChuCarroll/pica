package org.goodmath.pica.ast.action

import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.util.Symbol

class TypeAnalysisScope(private val enclosingScope: TypeAnalysisScope?) {
    private val bindings: MutableMap<Symbol, Type> = mutableMapOf()
    operator fun get(name: Symbol): Type? {
        return bindings[name] ?: enclosingScope?.get(name)
    }

    operator fun set(name: Symbol, t: Type) {
        bindings[name] = t
    }
}