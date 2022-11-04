package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.twist.Twist

enum class LiteralKind {
    StrLit, IntLit, FloatLit, CharLit, SymLit
}

class LiteralExpr(val kind: LiteralKind, val value: Any, loc: Location) : Expr(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Expr::Literal",
            Twist.attr("kind", kind.toString()),
            Twist.attr("value", value.toString())
        )
}