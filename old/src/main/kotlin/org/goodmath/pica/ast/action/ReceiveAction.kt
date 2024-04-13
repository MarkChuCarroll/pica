package org.goodmath.pica.ast.action

import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.defs.BosonPattern
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.expr.Expr
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.twist.Twist
import org.goodmath.pica.util.twist.arr
import org.goodmath.pica.util.twist.obj
import org.goodmath.pica.util.twist.value

class ReceiveClause(val pattern: BosonPattern, val body: List<Action>, loc: Location,
                    boundFrom: ReceiveClause? = null): AstNode(loc) {
    override fun twist(): Twist =
        obj(
            "Action::Receive::Clause",
            value("pattern", pattern),
            arr("body", body)
        )

    fun bind(typeEnv: Map<TypeVar, Type>): ReceiveClause {
        return ReceiveClause(pattern, body.map { it.bind(typeEnv)},
            loc, this)
    }
}

class ReceiveAction(val chan: Expr,
                    val matches: List<ReceiveClause>,
                    val elseAction: Action?,
                    loc: Location,
                    override val boundFrom: ReceiveAction? = null
): Action(loc) {
    override fun bind(typeEnv: Map<TypeVar, Type>): Action {
        return ReceiveAction(chan.bind(typeEnv),
            matches.map { it.bind(typeEnv) },
            elseAction?.bind(typeEnv),
            loc,
            this)
    }

    override fun twist(): Twist =
        obj(
            "Action::Receive",
            arr("matches", matches),
            value("elseAction", elseAction)
        )
}