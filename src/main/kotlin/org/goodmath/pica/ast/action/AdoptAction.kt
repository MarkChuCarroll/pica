package org.goodmath.pica.ast.action

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.expr.Expr
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist
import org.goodmath.pica.util.twist.arr
import org.goodmath.pica.util.twist.attr
import org.goodmath.pica.util.twist.obj

class AdoptAction(val behaviorName: Symbol, val args: List<Expr>, loc: Location,
    override val boundFrom: AdoptAction? = null): Action(loc) {
    override fun bind(typeEnv: Map<TypeVar, Type>): Action {
        val boundArgs = args.map { it.bind(typeEnv) }
        return AdoptAction(behaviorName, boundArgs, loc, this)
    }

    override fun twist(): Twist =
        obj(
            "Action::AdoptBehavior",
            attr("name", behaviorName.toString()),
            arr("args", args)
        )

}