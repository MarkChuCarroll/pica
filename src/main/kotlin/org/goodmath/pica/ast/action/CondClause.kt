package org.goodmath.pica.ast.action

import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.expr.Expr
import org.goodmath.pica.util.twist.Twist
import org.goodmath.pica.util.twist.arr
import org.goodmath.pica.util.twist.obj
import org.goodmath.pica.util.twist.value

class CondClause(val cond: Expr, val body: List<Action>, loc: Location): AstNode(loc) {
    override fun twist(): Twist =
        obj(
            "Action::Cond::CondClause",
            value("condition", cond),
            arr("body", body)
        )


}