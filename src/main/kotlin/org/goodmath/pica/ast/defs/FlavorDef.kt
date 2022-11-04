package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.SType
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class FlavorDef(
    hadronId: Identifier,
    name: Symbol,
    typeParams: List<TypeVar>?,
    val composes: List<SType>,
    val channels: List<ChannelDef>,
    loc: Location
): Definition(hadronId, name, typeParams, loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Def::Flavor",
            Twist.attr("hadron", hadronId.toString()),
            Twist.attr("name", name.repr),
            Twist.opt(typeParams?.let { Twist.arr("typeParams", it) }),
            Twist.arr("composes", composes),
            Twist.arr("channels", channels)
        )

}