package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.SType
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class BosonTupleOption(
    name: Symbol,
    val fields: List<SType>,
    loc: Location
): BosonOption(name, loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Def::Boson::TupleOption",
            Twist.attr("name", name.repr),
            Twist.arr("fields", fields)
        )

}