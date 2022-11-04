package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.twist.Twist

class HadronDefinition(val id: Identifier, val imports: List<UseDef>, val defs: List<Definition>):
        AstNode(Location(id.toString(), 0, 0)) {
    override fun twist(): Twist =
        Twist.obj(
            "HadronDefinition",
            Twist.attr("id", id.toString()),
            Twist.arr("uses", imports),
            Twist.arr("defs", defs)
        )
}