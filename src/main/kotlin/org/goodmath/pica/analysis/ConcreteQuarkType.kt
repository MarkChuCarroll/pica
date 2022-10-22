package org.goodmath.pica.analysis

import org.goodmath.pica.LocalScope
import org.goodmath.pica.RootScope
import org.goodmath.pica.Scope
import org.goodmath.pica.ast.QuarkDefinition
import org.goodmath.pica.ast.SType
import org.goodmath.pica.ast.TypeVar
import org.goodmath.pica.util.PicaErrorLog
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.Twist
import org.goodmath.pica.util.Twistable


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
    val typeArgs: List<ConcreteType>? = null,
): ConcreteType() {


    private val hadronScope = RootScope.getHadronScope(quarkDef.hadronId)!!
    override val scope by lazy {
        val s = LocalScope(hadronScope, quarkDef)

        if (typeArgs != null) {
            if (quarkDef.typeParams != null) {
                if (typeArgs.size != quarkDef.typeParams.size) {
                    throw PicaErrorLog.logException(
                        "${quarkDef.name} takes ${quarkDef.typeParams.size} type" +
                                "parameters, but ${typeArgs.size} were supplied"
                    )
                } else {
                    quarkDef.typeParams.zip(typeArgs).map { (param, arg) ->
                        // Validate that the parameter is valid.
                        for (c in param.constraint.orEmpty()) {
                            if (!arg.canSatisfy(c, s)) {
                                throw PicaErrorLog.logException(
                                    "${arg.toString()} cannot be used as a value for type parameter ${param.name}" +
                                            " to ${quarkDef.name}, because it does not satisfy the constraint ${c}"
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

    private val composedTypes: List<ConcreteQuarkType> by lazy {
        quarkDef.composes.map { ConcreteType.instantiate(it, scope) as ConcreteQuarkType }
    }



    val slots: List<ConcreteSlot> by lazy {
        val result = ArrayList<ConcreteSlot>()
        for (composedType in composedTypes) {
            result.addAll(composedType.slots)
        }
        for (slot in quarkDef.slots) {
            result.add(ConcreteSlot(slot.name, ConcreteType.instantiate(slot.type, scope)))
        }
        result
    }

    val channels: List<ConcreteChannel>by lazy {
        val result = ArrayList<ConcreteChannel>()
        for (composedType in composedTypes) {
            result.addAll(composedType.channels)
        }
        for (chan in quarkDef.channels) {
            result.add(ConcreteChannel(chan.name, ConcreteType.instantiate(chan.type, scope)))
        }
        result
    }


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

    override fun canSatisfy(constraint: SType, inScope: Scope): Boolean {
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