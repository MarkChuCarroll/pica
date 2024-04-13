package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class UseDef(val hadronId: Identifier, val names: List<Symbol>, loc: Location): AstNode(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Use",
            Twist.attr("hadron", hadronId.toString()),
            Twist.arr("names", names.map { Twist.leaf(it.repr) })
        )
}