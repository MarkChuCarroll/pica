package org.goodmath.pica.ast.action

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.expr.Expr
import org.goodmath.pica.util.twist.Twist
import org.goodmath.pica.util.twist.arr

class CondAction(
    val condClauses: List<CondClause>,
    val elseAction: List<Action>,
    loc: Location
): Action(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Action::Cond",
            arr("clauses", condClauses),
            arr("else", elseAction)
        )
}