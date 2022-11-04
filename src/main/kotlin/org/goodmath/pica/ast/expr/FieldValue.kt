package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class FieldValue(val name: Symbol, val value: Expr, loc: Location): AstNode(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Expr::Boson::FieldValue",
            Twist.attr("name", name.repr),
            Twist.value("value", value)
        )
}