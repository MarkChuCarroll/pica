package org.goodmath.pica.ast.action

import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.twist.Twist

class SeqAction(val body: List<Action>, loc: Location): Action(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Action::Seq",
            Twist.arr("body", body)
        )
}