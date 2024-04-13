package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class FieldValue(val name: Symbol, val value: Expr, loc: Location,
                 val boundFrom: FieldValue? = null): AstNode(loc) {

    fun bind(typeBindings: Map<TypeVar, Type>): FieldValue {
        return FieldValue(name, value.bind(typeBindings), loc,
            this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Expr::Boson::FieldValue",
            Twist.attr("name", name.repr),
            Twist.value("value", value)
        )
}