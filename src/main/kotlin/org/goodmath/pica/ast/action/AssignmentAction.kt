package org.goodmath.pica.ast.action

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.expr.Expr
import org.goodmath.pica.ast.expr.LValExpr
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.twist.Twist

class AssignmentAction(val target: LValExpr, val value: Expr, loc: Location,
                       override val boundFrom: AssignmentAction? = null): Action(loc) {
    override fun bind(typeEnv: Map<TypeVar, Type>): AssignmentAction {
        return AssignmentAction(target.bind(typeEnv), value.bind(typeEnv), loc, this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Action::Assignment",
            Twist.value("target", target),
            Twist.value("value", value)
        )

}