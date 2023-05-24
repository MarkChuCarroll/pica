package org.goodmath.pica.ast.action

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.expr.Expr
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.twist.Twist

class WhileAction(val cond: Expr, val body: List<Action>, loc: Location,
    override val boundFrom: WhileAction? = null): Action(loc) {
    override fun bind(typeEnv: Map<TypeVar, Type>): Action {
        return WhileAction(cond.bind(typeEnv), body.map { it.bind(typeEnv)}, loc,
            this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Action::While",
            Twist.value("cond", cond),
            Twist.arr("body", body)
        )
}