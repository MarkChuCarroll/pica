package org.goodmath.pica.ast.action

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.twist.Twist
import org.goodmath.pica.util.twist.arr

class CondAction(
    val condClauses: List<CondClause>,
    val elseAction: List<Action>,
    loc: Location,
    override val boundFrom: CondAction? = null
): Action(loc) {
    override fun bind(typeEnv: Map<TypeVar, Type>): Action {
        return CondAction(condClauses.map { it.bind(typeEnv) },
            elseAction.map { it.bind(typeEnv) },
            loc, this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Action::Cond",
            arr("clauses", condClauses),
            arr("else", elseAction)


        )
}