package org.goodmath.pica.analysis

import org.goodmath.pica.LocalScope
import org.goodmath.pica.RootScope
import org.goodmath.pica.Scope
import org.goodmath.pica.ast.*
import org.goodmath.pica.util.*

class ConcreteMessageType(
    val name: Symbol,
    sParams: List<TypedIdentifier>,
    val inScope: Scope): Twistable {

    val params: List<ConcreteType> by lazy {
        sParams.map { ConcreteType.instantiate(it.type, inScope) }
    }

    override fun twist(): Twist =
        Twist.obj("ConcreteType::Flavor::Message",
            Twist.attr("name", name.repr),
            Twist.arr("parameters", params))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConcreteMessageType

        if (name != other.name) return false
        if (params != other.params) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + params.hashCode()
        return result
    }
}

class ConcreteFlavorType(
    val flavorDef: FlavorDef,
    val typeArgs: List<ConcreteType>): ConcreteType() {

    private val hadronScope = RootScope.getHadronScope(flavorDef.hadronId)!!
    override val scope by lazy {
        val s = LocalScope(hadronScope, flavorDef)
        if (flavorDef.typeParams != null) {
            if (typeArgs.size != flavorDef.typeParams.size) {
                throw PicaErrorLog.logException(
                    PicaSyntaxException("${flavorDef.name} takes ${flavorDef.typeParams.size} type" +
                            "parameters, but ${typeArgs.size} were supplied", flavorDef.loc, flavorDef))
            } else {
                flavorDef.typeParams.zip(typeArgs).map { (param, arg) ->
                    // Validate that the parameter is valid.
                    for (c in param.constraint.orEmpty()) {
                        if (!arg.canSatisfy(c, s)) {
                            throw PicaErrorLog.logException(
                                PicaTypeException(
                                    "$arg cannot be used as a value for type parameter ${param.name}" +
                                            " to ${flavorDef.name}, because it does not satisfy the constraint $c",
                                    flavorDef.loc, flavorDef))
                        }
                        s.setType(param.name, arg)
                    }
                }
            }
        }
        s
    }

    val messages: List<ConcreteMessageType> by lazy {
        flavorDef.messages.map { ConcreteMessageType(it.name, it.params, scope) }
    }


    override fun canSatisfy(constraint: ConcreteType): Boolean {
        return when (constraint) {
            this -> {
                true
            }
            is ConcreteFlavorType -> {
                constraint.messages.all { it in messages }
            }
            is ConcreteTypeVar -> {
                constraint.constraints.all { canSatisfy(it) }
            }
            else -> {
                false
            }
        }
    }

    override fun twist(): Twist =
        Twist.obj("ConcreteType::Flavor",
            Twist.attr("id", flavorDef.id.toString()),
            Twist.opt(
                if (typeArgs.isNotEmpty()) { Twist.arr("typeArgs", typeArgs) }
                else { null }),
            Twist.arr("messages", messages))


}

