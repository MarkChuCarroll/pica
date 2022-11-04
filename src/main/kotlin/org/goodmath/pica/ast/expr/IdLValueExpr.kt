package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.twist.Twist

class IdLValueExpr(val id: Identifier, loc: Location) : LValExpr(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Expr::LVal::Id",
            Twist.attr("id", id.toString())
        )

}