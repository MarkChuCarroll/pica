package org.goodmath.pica.analysis

import org.goodmath.pica.LocalScope
import org.goodmath.pica.RootScope
import org.goodmath.pica.Scope
import org.goodmath.pica.ast.*
import org.goodmath.pica.util.PicaErrorLog
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.Twist
import org.goodmath.pica.util.Twistable
import java.util.Properties

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
    abstract val scope: Scope

    /**
     * Check if this concrete type can satisfy a type constraint.
     * To do this, we may need to be able to instantiate type arguments,
     * which requires a scope in which they can be looked up.
     */
    abstract fun canSatisfy(constraint: SType, inScope: Scope): Boolean


    companion object {

        fun instantiate(t: SType, scope: Scope): ConcreteType {
            if (t is NamedType) {
                val def = scope.getDefinition(t.typeId)
                if (def != null) {
                    val typeArgs = t.typeArgs?.map { instantiate(it, scope) }.orEmpty()
                    return when (def) {
                        is QuarkDefinition -> ConcreteQuarkType(def, typeArgs)
                        is BosonDefinition -> ConcreteBosonType(def, typeArgs)
                        is FlavorDef -> ConcreteFlavorType(def, typeArgs)
                        else -> {
                            throw PicaErrorLog.logException("Invalid declaration type.", null, def)
                        }
                    }
                } else { // If we have a named type, and it doesn't reference a definition,
                    // then either it's an undefined name (which should be an error), or it's
                    // a type variable. If the latter, then it should appear in the types of
                    // the scope.
                    val type =
                        scope.getType(t.typeId) ?: throw PicaErrorLog.logException("Undefined type name ${t.typeId}")
                    return type

                }
            } else {
                // It's a channel type.
                val chType = t as ChannelType
                return ConcreteChannelType(chType, chType.dir,
                    ConcreteType.instantiate(chType.messageType, scope), scope)
            }
        }
    }



}



class ConcreteFlavorType(
    val flavorDef: FlavorDef,
    val typeArgs: List<ConcreteType>): ConcreteType() {

    override val scope by lazy {
        TODO()
    }

    val channels: List<ConcreteChannel> by lazy {
        TODO()
    }


    override fun canSatisfy(constraint: SType, inScope: Scope): Boolean {
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
                typeArgs.map { t -> t.bind(bindings)}
            )
        }
    }
}


class ConcreteChannelType(val def: ChannelType,
                          val direction: ChannelDirection,
                          val msgType: ConcreteType, override val scope: Scope): ConcreteType() {
    override fun canSatisfy(constraint: SType, inScope: Scope): Boolean {
        TODO("Not yet implemented")
    }

    override fun twist(): Twist =
        Twist.obj("ConcreteType::Channel",
            Twist.attr("direction", direction.toString()),
            Twist.value("msgType", msgType)
        )

    override fun bind(bindings: Map<TypeVar, ConcreteType>): ConcreteChannelType =
        ConcreteChannelType(def, direction, msgType.bind(bindings), scope)

}

class ConcreteTypeVar(val name: Symbol, val def: TypeVar, override val scope: Scope): ConcreteType() {

    val id = "$name{${nextIndex()}}"

    val constraints: List<ConcreteType> by lazy {
        TODO()
    }

    override fun bind(bindings: Map<TypeVar, ConcreteType>): ConcreteType {
        return bindings[def] ?: this
    }

    override fun canSatisfy(constraint: SType, inScope: Scope): Boolean {
        TODO("Not yet implemented")
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
