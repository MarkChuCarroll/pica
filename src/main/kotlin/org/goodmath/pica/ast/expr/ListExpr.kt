package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.SType
import org.goodmath.pica.util.twist.Twist

class ListExpr(val type: SType, val values: List<Expr>, loc: Location) : Expr(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Expr::List",
            Twist.arr("values", values)
        )
}