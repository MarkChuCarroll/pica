package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.expr.Expr
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class SlotDef(
    val name: Symbol,
    val type: Type,
    val initValue: Expr,
    loc: Location,
    boundFrom: SlotDef? = null
): AstNode(loc) {

    fun bind(typeEnv: Map<TypeVar, Type>): SlotDef {
        return SlotDef(name, type.bind(typeEnv), initValue.bind(typeEnv), loc, this)
    }
    override fun twist(): Twist =
        Twist.obj(
            "Def::Quark::Slot",
            Twist.attr("name", name.repr),
            Twist.value("type", type),
            Twist.value("initValue", initValue)
        )

}