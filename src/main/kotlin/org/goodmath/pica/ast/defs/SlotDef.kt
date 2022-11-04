package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.expr.Expr
import org.goodmath.pica.ast.types.SType
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class SlotDef(
    val name: Symbol,
    val type: SType,
    val initValue: Expr,
    loc: Location
): AstNode(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Def::Quark::Slot",
            Twist.attr("name", name.repr),
            Twist.value("type", type),
            Twist.value("initValue", initValue)
        )

}