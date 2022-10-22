package org.goodmath.pica.analysis

import org.goodmath.pica.LocalScope
import org.goodmath.pica.RootScope
import org.goodmath.pica.Scope
import org.goodmath.pica.ast.*
import org.goodmath.pica.util.PicaErrorLog
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.Twist


abstract class ConcreteBosonOption(val name: Symbol): Bindable<ConcreteBosonOption> {
}

class ConcreteBosonTuple(val optionDef: BosonTupleOption, scope: Scope):
        ConcreteBosonOption(optionDef.name) {
    val fields: List<ConcreteType> by lazy {
        optionDef.fields.map { ConcreteType.instantiate(it, scope) }
    }

    override fun bind(bindings: Map<TypeVar, ConcreteType>): ConcreteBosonOption {
        TODO("Not yet implemented")
    }
}

class ConcreteBosonStruct(val optionDef: BosonStructOption, scope: Scope): ConcreteBosonOption(optionDef.name) {
    val fields: Map<Symbol, ConcreteType> =
        optionDef.fields.map { typedId ->  typedId.name to ConcreteType.instantiate(typedId.type, scope) }.toMap()

    override fun bind(bindings: Map<TypeVar, ConcreteType>): ConcreteBosonOption {
        TODO("Not yet implemented")
    }
}


class ConcreteBosonType(
    val bosonDef: BosonDefinition,
    val typeArgs: List<ConcreteType>
): ConcreteType() {

    private val hadronScope = RootScope.getHadronScope(bosonDef.hadronId)!!

    override val scope by lazy {
        val s = LocalScope(hadronScope, bosonDef)

        if (typeArgs != null) {
            if (bosonDef.typeParams != null) {
                if (typeArgs.size != bosonDef.typeParams.size) {
                    throw PicaErrorLog.logException(
                        "${bosonDef.name} takes ${bosonDef.typeParams.size} type" +
                                "parameters, but ${typeArgs.size} were supplied"
                    )
                } else {
                    bosonDef.typeParams.zip(typeArgs).map { (param, arg) ->
                        // Validate that the parameter is valid.
                        for (c in param.constraint.orEmpty()) {
                            if (!arg.canSatisfy(c, s)) {
                                throw PicaErrorLog.logException(
                                    "${arg.toString()} cannot be used as a value for type parameter ${param.name}" +
                                            " to ${bosonDef.name}, because it does not satisfy the constraint ${c}"
                                )
                            }
                            s.setType(param.name, arg)
                        }
                    }
                }
            }
        }
        s
    }


    val options: List<ConcreteBosonOption> by lazy {
        bosonDef.options.map { o ->
            when(o) {
                is BosonStructOption -> ConcreteBosonStruct(o, scope)
                is BosonTupleOption -> ConcreteBosonTuple(o, scope)
                else -> throw PicaErrorLog.logException("Invalid boson option $o")
            }
        }
    }

    /**
     * Two defined types are equal if they're the same object, or if they're built
     * from the same definition and the same type args.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return  other is ConcreteBosonType && bosonDef == other.bosonDef && typeArgs == other.typeArgs
    }

    /**
     * Hashcode needs to match our equality def above: only consider
     * typeargs and def.
     */
    override fun hashCode(): Int {
        var result = bosonDef.hashCode()
        result = 31 * result + typeArgs.hashCode()
        return result
    }


    override fun bind(bindings: Map<TypeVar, ConcreteType>): ConcreteType {
        return if (bosonDef.typeParams == null || bosonDef.typeParams.isEmpty()) {
            this
        } else {
            ConcreteBosonType(bosonDef, typeArgs.map { it.bind(bindings)})
            //, options.map { it.bind(bindings) })
        }
    }

    override fun canSatisfy(constraint: SType, inScope: Scope): Boolean {
        TODO("Not yet implemented")
    }

    override fun twist(): Twist =
        Twist.obj("ConcreteType::Boson",
            Twist.attr("id", bosonDef.id.toString()),
            Twist.value("bosonDef", bosonDef),
            Twist.opt(if (typeArgs.isNotEmpty()) {
                Twist.arr("typeArgs", typeArgs)
            } else {
                null
            }))
}

