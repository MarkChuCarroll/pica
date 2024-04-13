package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.twist.Twist

class IdLValueExpr(val id: Identifier, loc: Location,
    override val boundFrom: IdLValueExpr? = null) : LValExpr(loc) {

    override fun bind(typeBindings: Map<TypeVar, Type>): LValExpr {
        return IdLValueExpr(id, loc, this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Expr::LVal::Id",
            Twist.attr("id", id.toString())
        )

}