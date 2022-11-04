package org.goodmath.pica.ast.types

import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.twist.Twist

class NamedType(val typeId: Identifier, typeArgs: List<SType>, loc: Location): SType(typeArgs, loc) {
    override fun twist(): Twist =
        Twist.obj("Type::Named",
            Twist.attr("id", typeId.toString()),
            Twist.opt(typeArgs?.let { Twist.arr("typeArgs", it) })
        )
}