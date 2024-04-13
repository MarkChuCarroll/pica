package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.defs.BosonStructOption
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.twist.Twist

class BosonStructExpr(
    val bosonOptionName: Identifier,
    val fields: List<FieldValue>,
    loc: Location,
    override val boundFrom: BosonStructExpr? = null
) : Expr(loc) {
    override fun bind(typeBindings: Map<TypeVar, Type>): Expr {
        return BosonStructExpr(bosonOptionName, fields.map { it.bind(typeBindings)}, loc,
            boundFrom = this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Expr::Boson::Struct",
            Twist.attr("name", bosonOptionName.name.repr),
            Twist.arr("fields", fields)
        )

}