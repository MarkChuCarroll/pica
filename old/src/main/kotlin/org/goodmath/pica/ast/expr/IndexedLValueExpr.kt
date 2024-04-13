package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.twist.Twist

class IndexedLValueExpr(val baseLValue: LValExpr, val idx: Int, loc: Location,
    override val boundFrom: IndexedLValueExpr? = null) : LValExpr(loc) {

    override fun bind(typeBindings: Map<TypeVar, Type>): LValExpr {
        return IndexedLValueExpr(baseLValue.bind(typeBindings), idx, loc, this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Expr::LVal::Indexed",
            Twist.value("base", baseLValue),
            Twist.attr("index", idx.toString())
        )
}