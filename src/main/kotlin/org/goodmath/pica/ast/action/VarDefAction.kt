package org.goodmath.pica.ast.action

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.expr.Expr
import org.goodmath.pica.ast.types.SType
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class VarDefAction(val name: Symbol, val type: SType, val value: Expr, loc: Location): Action(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Action::VarDef",
            Twist.attr("name", name.repr),
            Twist.value("type", type),
            Twist.value("value", value)
        )

}