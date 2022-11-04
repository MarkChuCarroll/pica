package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class BosonDefinition(
    hadronId: Identifier,
    name: Symbol,
    typeParams: List<TypeVar>?,
    val options: List<BosonOption>,
    loc: Location
): Definition(hadronId, name, typeParams, loc) {

    override fun twist(): Twist =
        Twist.obj(
            "Def::Boson",
            Twist.attr("hadron", hadronId.toString()),
            Twist.attr("name", name.repr),
            Twist.opt(typeParams?.let { Twist.arr("typeParams", it) }),
            Twist.arr(
                "options",
                options
            )
        )



}