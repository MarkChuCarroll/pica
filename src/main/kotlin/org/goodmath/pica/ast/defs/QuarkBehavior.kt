package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.action.Action
import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.TypedIdentifier
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist
import org.goodmath.pica.util.twist.arr
import org.goodmath.pica.util.twist.obj

class QuarkBehavior(
    val name: Symbol,
    val args: List<TypedIdentifier>,
    val body: List<Action>,
    loc: Location
): AstNode(loc) {

    override fun twist(): Twist =
        obj(
            "Def::Quark::Behavior",
            arr("args", args),
            arr("body", body)
        )
}