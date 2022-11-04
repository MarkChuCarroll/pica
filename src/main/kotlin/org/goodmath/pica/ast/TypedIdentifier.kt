package org.goodmath.pica.ast

import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.SType
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class TypedIdentifier(
    val name: Symbol,
    val type: SType,
    loc: Location
): AstNode(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Param::TypedId",
            Twist.attr("name", name.repr),
            Twist.value("type", type)
        )

}