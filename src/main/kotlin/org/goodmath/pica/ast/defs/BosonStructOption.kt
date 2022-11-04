package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.TypedIdentifier
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class BosonStructOption(
    name: Symbol,
    val fields: List<TypedIdentifier>,
    loc: Location
): BosonOption(name, loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Def::Boson::StructOption",
            Twist.attr("name", name.repr),
            Twist.arr("fields", fields)
        )
}