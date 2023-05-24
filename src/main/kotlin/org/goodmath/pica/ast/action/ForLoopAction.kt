package org.goodmath.pica.ast.action

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.expr.Expr
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class ForLoopAction(val index: Symbol,
                    val collection: Expr,
                    val body: List<Action>,
                    loc: Location,
                    override val boundFrom: ForLoopAction? = null): Action(loc) {
    override fun bind(typeEnv: Map<TypeVar, Type>): Action {
        return ForLoopAction(index, collection.bind(typeEnv), body.map { it.bind(typeEnv)}, loc,
            this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Action::ForLoop",
            Twist.attr("index", index.toString()),
            Twist.value("collection", collection),
            Twist.arr("body", body)
        )
}