package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.twist.Twist

class ListExpr(val type: Type, val values: List<Expr>, loc: Location,
               override val boundFrom: ListExpr? = null): Expr(loc) {

    override fun bind(typeBindings: Map<TypeVar, Type>): Expr {
        return ListExpr(type.bind(typeBindings), values.map { it.bind(typeBindings) },
            loc, this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Expr::List",
            Twist.arr("values", values)
        )
}