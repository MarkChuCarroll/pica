package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.ChannelType
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist
import org.goodmath.pica.util.twist.attr
import org.goodmath.pica.util.twist.obj
import org.goodmath.pica.util.twist.value

class ChannelDef(val name: Symbol, val type: ChannelType, loc: Location): AstNode(loc) {
    override fun twist(): Twist =
        obj(
            "ChannelDef",
            attr("name", name.toString()),
            value("type", type)
        )

}