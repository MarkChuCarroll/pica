package org.goodmath.pica.ast.action

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.expr.Expr
import org.goodmath.pica.util.twist.Twist

class SendAction(val target: Expr, val msg: Expr, loc: Location): Action(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Action::Send",
            Twist.value("target", target),
            Twist.value("message", msg)
        )
}