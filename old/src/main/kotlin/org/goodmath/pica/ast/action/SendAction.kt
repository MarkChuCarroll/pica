package org.goodmath.pica.ast.action

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.expr.Expr
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.twist.Twist

class SendAction(val target: Expr, val msg: Expr, loc: Location,
                 override val boundFrom: SendAction? = null): Action(loc) {
    override fun bind(typeEnv: Map<TypeVar, Type>): Action {
        return SendAction(target.bind(typeEnv), msg.bind(typeEnv), loc, this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Action::Send",
            Twist.value("target", target),
            Twist.value("message", msg)
        )
}