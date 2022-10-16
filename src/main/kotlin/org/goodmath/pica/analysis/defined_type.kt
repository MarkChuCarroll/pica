package org.goodmath.pica.analysis

import org.goodmath.pica.ast.Definition
import org.goodmath.pica.ast.QuarkDefinition
import org.goodmath.pica.ast.TypeVar
import org.goodmath.pica.util.PicaErrorLog

abstract class DefinedType(val definition: Definition) {
    abstract fun instantiate(typeArgs: List<ConcreteType>): ConcreteType


    fun validateTypeArgsAndCreateBindings(typeArgs: List<ConcreteType>): Map<TypeVar, ConcreteType> {
        if (typeArgs.size != definition.typeParams?.size) {
            throw PicaErrorLog.logException(
                "Length of type params for type ${definition.name} (${definition.typeParams?.size} doesn't match length"
                        + " of supplied type arguments (${typeArgs.size}", loc = definition.loc
            )
        }
        val bindings: Map<TypeVar, ConcreteType> = definition.typeParams.zip(typeArgs).toMap()


        for ((typeParam, typeArg)  in bindings) {
            if (typeParam.constraint != null) {
                for (const in typeParam.constraint) {
                    if (!typeArg.canSatisfy(const)) {
                        throw PicaErrorLog.logException(
                            "Type argument ${typeArg} doesn't satisfy the constraint $const of ${typeParam.name} " +
                                    "in ${definition.name}"
                        )
                    }
                }
            }
        }
        return bindings
    }
}

class DefinedQuarkType(val quarkDef: QuarkDefinition): DefinedType(quarkDef) {
    // list of existing instantiations. If we've ever generated an instantiation for
    // a given set of type args, then we reuse it.
    private val instantiations = HashMap<List<ConcreteType>, ConcreteQuarkType>()


    private fun validateAndRetrieveComposedTypes(): List<ConcreteQuarkType> {
        return quarkDef.composes.map { c ->
            val composedObjectType = ConcreteType.getConcreteTypeFor(c)
            if (composedObjectType !is ConcreteQuarkType) {
                throw PicaErrorLog.logException(
                    "compose values of a quark must be quarks, but found ${composedObjectType}",
                    loc = c.loc, astNode = c
                )
            } else {
                composedObjectType
            }
        }
    }

    override fun instantiate(typeArgs: List<ConcreteType>): ConcreteType {
        TODO("Not yet implemented")
    }
    /*
        override fun instantiate(typeArgs: List<ConcreteType>): ConcreteQuarkType {
            return instantiations[typeArgs] ?: run {
                val concreteComposedTypes = validateAndRetrieveComposedTypes()
                val slots: List<ConcreteSlot> = concreteComposedTypes.flatMap { c -> c.slots } +
                        quarkDef.slots.map { slot -> ConcreteSlot(slot.name, ConcreteType.getConcreteTypeFor(slot.type)) }
                val channels: List<ConcreteChannel> = concreteComposedTypes.flatMap { c -> c.channels } +
                        quarkDef.channels.map { chan ->
                            ConcreteChannel(
                                chan.name,
                                ConcreteType.getConcreteTypeFor(chan.type)
                            )
                        }

                ConcreteQuarkType(quarkDef, typeArgs, slots, channels)
            }
        }

     */
}
