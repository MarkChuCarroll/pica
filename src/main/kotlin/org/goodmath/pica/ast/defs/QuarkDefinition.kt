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
    val body: List<Action>,
    loc: Location,
    val boundFrom: QuarkDefinition? = null
): Definition(hadronId, name, typeParams, loc) {
    override fun instantiate(typeArgs: List<Type>): Definition {
        validateTypeParameters(typeArgs)
        val typeEnv = typeParams.zip(typeArgs).associate { (typeVar, concreteType) -> typeVar to concreteType}
        return QuarkDefinition(hadronId, name, emptyList(),
            valueParams.map { it.bind(typeEnv) },
            flavors.map { it.bind(typeEnv) },
            channels.map { it.bind(typeEnv) },
            slots.map { it.bind(typeEnv) },
            behaviors.map { it.bind(typeEnv) },
            body.map { it.bind(typeEnv) },
            loc, this)

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
            Twist.arr("body", body)
        )
}