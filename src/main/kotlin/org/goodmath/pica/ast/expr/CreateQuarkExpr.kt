package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.SType
import org.goodmath.pica.util.twist.Twist

class CreateQuarkExpr(
    val quarkType: SType,
    val valueArgs: List<Expr>,
    loc: Location
) : Expr(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Expr::CreateQuark",
            Twist.value("type", quarkType),
            Twist.arr("valueArgs", valueArgs)
        )
}