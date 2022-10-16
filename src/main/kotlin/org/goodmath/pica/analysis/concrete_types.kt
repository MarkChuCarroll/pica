package org.goodmath.pica.analysis

import org.goodmath.pica.LocalScope
import org.goodmath.pica.RootScope
import org.goodmath.pica.ast.*
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.Twist
import org.goodmath.pica.util.Twistable

/**
 * How are parametric types going to work in here?
 *
 * When you analyze a quark definition, you create typevars for each of its type parameters, and
 * setting them in the scope of the definition. For example, if you're instantiating a
 * quark, then you create a new local scope from the quark (and, of course, it's static
 * module), and then you insert the type variables into that new local scope as the
 * values of the type arguments. When you construct other concrete types in that scope,
 * they'll just look up the values of those type names, and get back the type variables.
 *
 */

interface Bindable<T> {
    fun bind(bindings: Map<TypeVar, ConcreteType>): T
}

abstract class ConcreteType(): Twistable, Bindable<ConcreteType> {

    /**
     * Check if the type params are valid for this type.
     * This means checking that all constraints are satisfied.
     */
    fun validTypeParams(): Boolean {
        TODO()
    }


    /**
     * Check if this concrete type can satisfy a type constraint.
     */
    abstract fun canSatisfy(constraint: SType): Boolean

    companion object {
        fun getConcreteTypeFor(t: SType): ConcreteType {
            TODO()
        }

        fun getConcreteTypeFor(t: SType, typeArgs: List<ConcreteType>): ConcreteType {
            TODO()
        }
    }
}

data class ConcreteSlot(val name: Symbol, val type: ConcreteType): Twistable, Bindable<ConcreteSlot> {
    override fun twist(): Twist =
        Twist.obj("ConcreteSlot",
        Twist.attr("name", name.toString()),
            Twist.value("type", type))

    override fun bind(bindings: Map<TypeVar, ConcreteType>): ConcreteSlot {
        return ConcreteSlot(name, type.bind(bindings))
    }

}

data class ConcreteChannel(val name: Symbol, val type: ConcreteType): Twistable, Bindable<ConcreteChannel> {
    override fun twist(): Twist =
        Twist.obj("ConcreteChannel",
            Twist.attr("name", name.toString()),
            Twist.value("type", type))

    override fun bind(bindings: Map<TypeVar, ConcreteType>): ConcreteChannel {
        return ConcreteChannel(name, type.bind(bindings))
    }
}

class ConcreteQuarkType(
    val quarkDef: QuarkDefinition,
    val typeArgs: List<ConcreteType>,
    val slots: List<ConcreteSlot>,
    val channels: List<ConcreteChannel>
): ConcreteType() {

    val scope = LocalScope(RootScope.getHadronScope(quarkDef.hadronId)!!, quarkDef)

    /**
     * Two defined types are equal if they're the same object, or if they're built
     * from the same definition and the same type args.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return  other is ConcreteQuarkType && quarkDef == other.quarkDef && typeArgs == other.typeArgs
    }

    /**
     * Hashcode needs to match our equality def above: only consider
     * typeargs and def.
     */
    override fun hashCode(): Int {
        var result = quarkDef.hashCode()
        result = 31 * result + typeArgs.hashCode()
        return result
    }

    override fun bind(bindings: Map<TypeVar, ConcreteType>): ConcreteType {
        TODO("Not yet implemented")
    }

    override fun canSatisfy(constraint: SType): Boolean {
        TODO("Not yet implemented")
    }


    override fun twist(): Twist =
        Twist.obj("ConcreteType::Quark",
            Twist.attr("id", quarkDef.id.toString()),
            Twist.value("quarkDef", quarkDef),
            Twist.opt(typeArgs ?.let { Twist.arr("typeArgs", it) }))

//    fun implements(flavor: ConcreteFlavorType): Boolean {
//        TODO()
//    }

}


class ConcreteFlavorType(
    val flavorDef: FlavorDef,
    val typeArgs: List<ConcreteType>,
    val channels: List<ConcreteChannel>
): ConcreteType() {
    override fun canSatisfy(constraint: SType): Boolean {
        TODO("Not yet implemented")
    }

    override fun twist(): Twist =
        Twist.obj("ConcreteType::Flavor",
            Twist.attr("id", flavorDef.id.toString()),
            Twist.opt(
                if (typeArgs.isNotEmpty()) { Twist.arr("typeArgs", typeArgs) }
                else { null }),
            Twist.arr("channels", channels))

    override fun bind(bindings: Map<TypeVar, ConcreteType>): ConcreteFlavorType {
        return if (flavorDef.typeParams == null || flavorDef.typeParams.isEmpty()) {
            this
        } else {
            return ConcreteFlavorType(flavorDef,
                typeArgs.map { t -> t.bind(bindings)},
                channels.map { c -> c.bind(bindings) }
            )
        }
    }
}

abstract class ConcreteBosonOption(val name: Symbol): Bindable<ConcreteBosonOption> {
}

class ConcreteBosonTuple(name: Symbol, val fields: List<ConcreteType>): ConcreteBosonOption(name) {
    override fun bind(bindings: Map<TypeVar, ConcreteType>): ConcreteBosonTuple {
        return ConcreteBosonTuple(name, fields.map { it.bind(bindings) })
    }
}

class ConcreteBosonStruct(name: Symbol, val fields: Map<Symbol, ConcreteType>): ConcreteBosonOption(name) {
    override fun bind(bindings: Map<TypeVar, ConcreteType>): ConcreteBosonStruct {
        return ConcreteBosonStruct(name, fields.map { (name, type) -> Pair(name, type.bind(bindings)) }.toMap())
    }
}


class ConcreteBosonType(
    val bosonDef: BosonDefinition,
    val typeArgs: List<ConcreteType>,
    val options: List<ConcreteBosonOption>
): ConcreteType() {

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
            ConcreteBosonType(bosonDef, typeArgs.map { it.bind(bindings)}, options.map { it.bind(bindings) })
        }
    }

    override fun canSatisfy(constraint: SType): Boolean {
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

class ConcreteTypeVar(val name: Symbol, val def: TypeVar): ConcreteType() {

    val id = "$name{${nextIndex()}}"

    val constraints: List<ConcreteType> by lazy {
        TODO()
    }

    override fun bind(bindings: Map<TypeVar, ConcreteType>): ConcreteType {
        return bindings[def] ?: this
    }

    override fun canSatisfy(constraint: SType): Boolean {
        return (def.constraint != null && constraint in  def.constraint)
    }

    override fun twist(): Twist =
        Twist.obj("TypeVar",
            Twist.value("definition", def))

    companion object {
        var currentMaxIndex: Int = 0
        private fun nextIndex(): Int {
            currentMaxIndex++
            return currentMaxIndex
        }
    }
}
