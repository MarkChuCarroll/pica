package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.twist.Twist

class CreateQuarkExpr(
    val quarkType: Type,
    val valueArgs: List<Expr>,
    loc: Location,
    override val boundFrom: CreateQuarkExpr? = null
) : Expr(loc) {

    override fun bind(typeBindings: Map<TypeVar, Type>): Expr {
        return CreateQuarkExpr(quarkType.bind(typeBindings),
            valueArgs.map { it.bind(typeBindings) },
            loc,
            boundFrom = this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Expr::CreateQuark",
            Twist.value("type", quarkType),
            Twist.arr("valueArgs", valueArgs)
        )
}