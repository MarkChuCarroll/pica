package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.twist.Twist

class CondExpr(val cond: Expr, val onTrue: Expr, val onFalse: Expr, loc: Location,
               override val boundFrom: CondExpr? = null): Expr(loc) {

    override fun bind(typeBindings: Map<TypeVar, Type>): CondExpr {
        return CondExpr(cond.bind(typeBindings), onTrue.bind(typeBindings), onFalse.bind(typeBindings),
            loc, boundFrom = this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Expr::Cond",
            Twist.value("cond", cond),
            Twist.value("onTrue", onTrue),
            Twist.value("onFalse", onFalse)
        )

}