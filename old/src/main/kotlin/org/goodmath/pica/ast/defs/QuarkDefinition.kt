package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.*
import org.goodmath.pica.ast.action.Action
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class QuarkDefinition(
    hadronId: Identifier,
    name: Symbol,
    typeParams: List<TypeVar>,
    val valueParams: List<TypedIdentifier>,
    val flavors: List<Type>,
    val channels: List<ChannelDef>,
    val slots: List<SlotDef>,
    val behaviors: List<QuarkBehavior>,
    loc: Location,
    val boundFrom: QuarkDefinition? = null
): Definition(hadronId, name, typeParams, loc) {

    init {
        behaviors.map { it.setQuarkDefinition(this) }
        channels.map { it.setOuterDefinition(this) }
    }

    override fun isFullyConcrete(): Boolean {
        return typeParams.isEmpty() &&
                flavors.all { it.isFullyConcrete() } &&
                slots.all { it.type.isFullyConcrete() }
    }

    override fun instantiate(typeArgs: List<Type>): QuarkDefinition {
        validateTypeParameters(typeArgs)
        return QuarkDefinition.registry.getOrCreateInstantiation(this, typeArgs)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Def::Quark",
            Twist.attr("hadron", hadronId.toString()),
            Twist.attr("name", name.repr),
            Twist.arr("typeParams", typeParams),
            Twist.arr("valueParams", valueParams),
            Twist.arr("channels", channels),
            Twist.arr("flavors", flavors),
            Twist.arr("behaviors", behaviors),
            Twist.arr("slots", slots),
        )

    companion object {
        private fun instantiator(quark: QuarkDefinition, instantiationParameters: List<Type>): QuarkDefinition {
            val typeEnv = quark.typeParams.zip(instantiationParameters)
                .associate { (typeVar, concreteType) -> typeVar to concreteType }
            return QuarkDefinition(
                quark.hadronId, quark.name, emptyList(),
                quark.valueParams.map { it.bind(typeEnv) },
                quark.flavors.map { it.bind(typeEnv) },
                quark.channels.map { it.bind(typeEnv) },
                quark.slots.map { it.bind(typeEnv) },
                quark.behaviors.map { it.bind(typeEnv) },
                quark.loc, quark
            )
        }

        val registry = InstantiationRegistry(this::instantiator)
    }
}

