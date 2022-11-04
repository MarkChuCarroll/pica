package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class FieldLValueExpr(val baseLValue: LValExpr, val field: Symbol, loc: Location) : LValExpr(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Expr::LVal::Field",
            Twist.value("base", baseLValue),
            Twist.attr("field", field.repr)
        )
}