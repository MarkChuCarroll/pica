package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.twist.Twist

class BosonStructExpr(
    val bosonOptionName: Identifier,
    val fields: List<FieldValue>,
    loc: Location
) : Expr(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Expr::Boson::Struct",
            Twist.attr("name", bosonOptionName.name.repr),
            Twist.arr("fields", fields)
        )

}