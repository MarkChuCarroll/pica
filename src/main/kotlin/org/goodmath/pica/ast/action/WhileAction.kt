package org.goodmath.pica.ast.action

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.expr.Expr
import org.goodmath.pica.util.twist.Twist

class WhileAction(val cond: Expr, val body: List<Action>, loc: Location): Action(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Action::While",
            Twist.value("cond", cond),
            Twist.arr("body", body)
        )
}