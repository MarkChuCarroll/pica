package org.goodmath.pica.analysis

import org.goodmath.pica.ast.defs.Definition
import org.goodmath.pica.ast.types.SType

/**
 * Each time a type is instantiated as a concrete type, we need to create
 * a record for the pair of (definition, typeargs). For each unique combination
 * of definition and typeargs, there should be only one instance - so we need to be
 * able to ask for a concrete type, and either create or get the already existing instance.
 */
object TypeRegistry {

    private val byDef = HashMap<Definition, MutableList<ConcreteDefinedType>>()

    fun getConcreteType(sType: SType): ConcreteType {
        TODO()
    }

    fun getConcreteType(d: Definition, typeArgs: List<ConcreteType>): ConcreteDefinedType {
        val types = byDef[d] ?: run {
            val lst = ArrayList<ConcreteDefinedType>()
            byDef[d] = lst
            lst
        }

        val t = types.firstOrNull {
            it.typeArgs == typeArgs
        }
        return if (t == null) {
            val result = ConcreteDefinedType(d, typeArgs)
            types.add(result)
            result
        } else {
            t
        }
    }

}