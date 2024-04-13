package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class FieldLValueExpr(val baseLValue: LValExpr, val field: Symbol, loc: Location,
    override val boundFrom: FieldLValueExpr? = null) : LValExpr(loc) {
    override fun bind(typeBindings: Map<TypeVar, Type>): LValExpr {
        return FieldLValueExpr(baseLValue.bind(typeBindings), field, loc, this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Expr::LVal::Field",
            Twist.value("base", baseLValue),
            Twist.attr("field", field.repr)
        )
}