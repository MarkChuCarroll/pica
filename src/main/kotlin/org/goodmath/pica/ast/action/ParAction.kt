package org.goodmath.pica.ast.action

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.twist.Twist

class ParAction(val body: List<Action>, loc: Location,
                override val boundFrom: ParAction? = null): Action(loc) {
    override fun bind(typeEnv: Map<TypeVar, Type>): Action {
        return ParAction(body.map{it.bind(typeEnv)}, loc, this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Action::Par",
            Twist.arr("body", body)
        )
}