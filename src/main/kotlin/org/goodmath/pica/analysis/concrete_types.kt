package org.goodmath.pica.analysis

import org.goodmath.pica.RootScope
import org.goodmath.pica.Scope
import org.goodmath.pica.ast.*
import org.goodmath.pica.util.*


abstract class ConcreteType(): Twistable {
    abstract val scope: Scope


    /**
     * Check if this concrete type can satisfy a type constraint.
     * To do this, we may need to be able to instantiate type arguments,
     * which requires a scope in which they can be looked up.
     */
    fun canSatisfy(constraint: SType, inScope: Scope): Boolean =
        canSatisfy(instantiate(constraint, inScope))

    abstract fun canSatisfy(constraint: ConcreteType): Boolean


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
                            throw PicaErrorLog.logException(
                                PicaTypeException("Invalid declaration type; expected one of quark, flavor, or boson," +
                                        "but saw ${def}",
                                    def.loc, def
                                ))

                        }
                    }
                } else { // If we have a named type, and it doesn't reference a definition,
                    // then either it's an undefined name (which should be an error), or it's
                    // the name of a type variable. If the latter, then it should appear in the types of
                    // the scope.
                    val type =
                        scope.getType(t.typeId) ?: throw PicaErrorLog.logException(
                            PicaTypeException("Undefined type name ${t.typeId}", t.loc, t))
                    return type
                }
            } else {
                // Types are either named types, or type variables. If we're trying to
                // analyze a type, and a type variable hasn't been replaced, then it's
                // an internal error - we messed up, it should have been replaced during instantiation.
                throw PicaErrorLog.logException(
                    PicaTypeException("Naked type variable ${t}", t.loc, t))
            }
        }
    }
}

// TODO: Can a type var take type params? I don't think so, that would
// be higher order types, which I don't want to deal with.
class ConcreteTypeVar(val name: Symbol, val def: TypeVar, override val scope: Scope): ConcreteType() {

    val id = "$name{${nextIndex()}}"

    val constraints: List<ConcreteType> by lazy {
        def.constraint.orEmpty().map { instantiate(it, scope) }
    }

    val messages: List<ConcreteMessageType> by lazy {
        constraints.flatMap {
            when(it) {
                is ConcreteTypeVar -> it.messages
                is ConcreteFlavorType -> it.messages
                else -> emptyList()
            }
        }
    }

    override fun canSatisfy(constraint: ConcreteType): Boolean {
        return when(constraint) {
            is ConcreteFlavorType -> constraint.messages.containsAll(messages)
            is ConcreteTypeVar -> constraint.messages.containsAll(messages)
            else -> false
        }
    }

    override fun twist(): Twist =
        Twist.obj("TypeVar",
            Twist.value("definition", def))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConcreteTypeVar

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        var currentMaxIndex: Int = 0
        private fun nextIndex(): Int {
            currentMaxIndex++
            return currentMaxIndex
        }
    }
}
