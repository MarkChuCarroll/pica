package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar

abstract class LValExpr(loc: Location) : Expr(loc) {
    abstract override fun bind(typeBindings: Map<TypeVar, Type>): LValExpr

}