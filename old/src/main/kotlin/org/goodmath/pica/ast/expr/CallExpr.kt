package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.twist.Twist

class CallExpr(val target: Expr, val args: List<Expr>, loc: Location,
               override val boundFrom: CallExpr? = null): Expr(loc) {
    override fun bind(typeBindings: Map<TypeVar, Type>): Expr {
        val boundTarget = target.bind(typeBindings)
        val boundArgs = args.map { it.bind(typeBindings) }
        return if (boundTarget != target || args != boundArgs) {
            CallExpr(boundTarget, boundArgs, loc, boundFrom=this)
        } else {
            this
        }
    }

    override fun twist(): Twist {
        return Twist.obj("expr:call",
            Twist.value("target", target),
            Twist.arr("args", args))

    }
}