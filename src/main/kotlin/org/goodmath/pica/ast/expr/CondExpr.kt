package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.twist.Twist

class CondExpr(val cond: Expr, val onTrue: Expr, val onFalse: Expr, loc: Location): Expr(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Expr::Cond",
            Twist.value("cond", cond),
            Twist.value("onTrue", onTrue),
            Twist.value("onFalse", onFalse)
        )

}