package org.goodmath.pica.ast.action

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.expr.Expr
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class VarDefAction(val name: Symbol, val type: Type, val value: Expr, loc: Location,
    override val boundFrom: VarDefAction? = null
): Action(loc) {
    override fun bind(typeEnv: Map<TypeVar, Type>): Action {
        return VarDefAction(name, type.bind(typeEnv), value.bind(typeEnv), loc, this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Action::VarDef",
            Twist.attr("name", name.repr),
            Twist.value("type", type),
            Twist.value("value", value)
        )

}