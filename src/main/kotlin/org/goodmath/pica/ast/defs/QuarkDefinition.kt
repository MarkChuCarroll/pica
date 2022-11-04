package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.*
import org.goodmath.pica.ast.action.Action
import org.goodmath.pica.ast.types.SType
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist
class QuarkDefinition(
    hadronId: Identifier,
    name: Symbol,
    typeParams: List<TypeVar>,
    val valueParams: List<TypedIdentifier>,
    val flavors: List<SType>,
    val channels: List<ChannelDef>,
    val slots: List<SlotDef>,
    val behaviors: List<QuarkBehavior>,
    val body: List<Action>,
    loc: Location
): Definition(hadronId, name, typeParams, loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Def::Quark",
            Twist.attr("hadron", hadronId.toString()),
            Twist.attr("name", name.repr),
            Twist.opt(typeParams?.let { Twist.arr("typeParams", it) }),
            Twist.arr("valueParams", valueParams),
            Twist.arr("channels", channels),
            Twist.arr("flavors", flavors),
            Twist.arr("behaviors", behaviors),
            Twist.arr("slots", slots),
            Twist.arr("body", body)
        )
}