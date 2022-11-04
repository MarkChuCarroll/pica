package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.twist.Twist

class IndexedLValueExpr(val baseLValue: LValExpr, val idx: Int, loc: Location) : LValExpr(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Expr::LVal::Indexed",
            Twist.value("base", baseLValue),
            Twist.attr("index", idx.toString())
        )
}