package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.twist.Twist

enum class LiteralKind {
    StrLit, IntLit, FloatLit, CharLit, SymLit
}

class LiteralExpr(val kind: LiteralKind, val value: Any, loc: Location,
    override val boundFrom: LiteralExpr? = null) : Expr(loc) {

    override fun bind(typeBindings: Map<TypeVar, Type>): Expr {
        return this
    }

    override fun twist(): Twist =
        Twist.obj(
            "Expr::Literal",
            Twist.attr("kind", kind.toString()),
            Twist.attr("value", value.toString())
        )
}