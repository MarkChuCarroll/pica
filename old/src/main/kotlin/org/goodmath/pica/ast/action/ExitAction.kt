package org.goodmath.pica.ast.action

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.twist.Twist

class ExitAction(loc: Location,
                 override val boundFrom: ExitAction? = null): Action(loc) {
    override fun bind(typeEnv: Map<TypeVar, Type>): Action {
        return this
    }

    override fun twist(): Twist {
        return Twist.obj("action::exit")
    }
}