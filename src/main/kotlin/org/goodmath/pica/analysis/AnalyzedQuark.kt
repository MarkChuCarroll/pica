package org.goodmath.pica.analysis

import org.goodmath.pica.ast.defs.QuarkDefinition

class AnalyzedQuark(val def: QuarkDefinition, optTypeArgs: List<ConcreteType>? = null) {
    val typeArgs = optTypeArgs ?: def.typeParams.map { ConcreteTypeVariable(it) }

    fun instantiate(bindings: Map<ConcreteTypeVariable, ConcreteType>): AnalyzedQuark {
        val boundTypeArgs = typeArgs.map { it.bind(bindings) }
        return AnalyzedQuark(def, boundTypeArgs)
    }

    val concreteType: ConcreteDefinedType = ConcreteDefinedType(def, typeArgs)

    val channels: List<ConcreteChannelType> by lazy {
        TODO()
    }

}

