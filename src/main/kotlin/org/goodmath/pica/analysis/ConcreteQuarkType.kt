package org.goodmath.pica.analysis

import org.goodmath.pica.LocalScope
import org.goodmath.pica.RootScope
import org.goodmath.pica.ast.QuarkDefinition
import org.goodmath.pica.util.*


data class ConcreteSlot(val name: Symbol, val type: ConcreteType): Twistable {
    override fun twist(): Twist =
        Twist.obj("ConcreteSlot",
            Twist.attr("name", name.toString()),
            Twist.value("type", type))
}

class ConcreteQuarkType(
    val quarkDef: QuarkDefinition,
    val typeArgs: List<ConcreteType>? = null
): ConcreteType() {
    private val hadronScope = RootScope.getHadronScope(quarkDef.hadronId)!!
    override val scope by lazy {
        val s = LocalScope(hadronScope, quarkDef)
        if (typeArgs != null) {
            if (quarkDef.typeParams != null) {
                if (typeArgs.size != quarkDef.typeParams.size) {
                    throw PicaErrorLog.logException(
                        PicaTypeException("${quarkDef.name} takes ${quarkDef.typeParams.size} type" +
                                "parameters, but ${typeArgs.size} were supplied",
                            quarkDef.loc,
                            quarkDef))
                } else {
                    quarkDef.typeParams.zip(typeArgs).map { (param, arg) ->
                        // Validate that the parameter is valid.
                        for (c in param.constraint.orEmpty()) {
                            if (!arg.canSatisfy(c, s)) {
                                throw PicaErrorLog.logException(
                                    PicaTypeException(
                                        "$arg cannot be used as a value for type parameter ${param.name}" +
                                                " to ${quarkDef.name}, because it does not satisfy the constraint $c",
                                        quarkDef.loc, quarkDef))
                            }
                            s.setType(param.name, arg)
                        }
                    }
                }
            }
        }
        s
    }

    private val composedTypes: List<ConcreteQuarkType> by lazy {
        quarkDef.composes.map { instantiate(it.quarkType, scope) as ConcreteQuarkType }
    }

    val slots: List<ConcreteSlot> by lazy {
        val result = ArrayList<ConcreteSlot>()
        for (composedType in composedTypes) {
            result.addAll(composedType.slots)
        }
        for (slot in quarkDef.slots) {
            result.add(ConcreteSlot(slot.name, instantiate(slot.type, scope)))
        }
        result
    }

    val messages: List<ConcreteMessageType> by lazy {
        quarkDef.messages.map {
            ConcreteMessageType(it.name, it.params, scope)
        }
    }

    /**
     * Two defined types are equal if they're the same object, or if they're built
     * from the same definition and the same type args.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is ConcreteQuarkType && quarkDef == other.quarkDef && typeArgs == other.typeArgs
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

    override fun canSatisfy(constraint: ConcreteType): Boolean {
        return when(constraint) {
            is ConcreteQuarkType ->
                constraint in composedTypes
            is ConcreteFlavorType ->
                constraint.messages.all { it in messages }
            is ConcreteTypeVar ->
                constraint.constraints.all { canSatisfy(it) }
            else -> false
        }
    }

    override fun twist(): Twist =
        Twist.obj(
            "ConcreteType::Quark",
            Twist.attr("id", quarkDef.id.toString()),
            Twist.attr("quarkDef", quarkDef.id.toString()),
            Twist.opt(typeArgs?.let { Twist.arr("typeArgs", it) }),
            Twist.arr("slots", slots),
            Twist.arr("messages", messages)
        )
}
