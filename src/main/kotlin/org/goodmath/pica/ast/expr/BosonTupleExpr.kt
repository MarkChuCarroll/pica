package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.defs.BosonTupleOption
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.twist.Twist

class BosonTupleExpr(val bosonOptionName: Identifier, val fields: List<Expr>, loc: Location,
    override val boundFrom: BosonTupleExpr? = null) : Expr(loc) {

    override fun bind(typeBindings: Map<TypeVar, Type>): BosonTupleExpr {
        return BosonTupleExpr(bosonOptionName, fields.map { it.bind(typeBindings) },
            loc, this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Expr::Boson::Tuple",
            Twist.attr("name", bosonOptionName.name.repr),
            Twist.arr("fields", fields)
        )
}