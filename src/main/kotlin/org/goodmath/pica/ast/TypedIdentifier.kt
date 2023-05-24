package org.goodmath.pica.ast

import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class TypedIdentifier(
    val name: Symbol,
    val type: Type,
    loc: Location
): AstNode(loc) {

    fun bind(typeEnv: Map<TypeVar, Type>): TypedIdentifier {
        return TypedIdentifier(name, type.bind(typeEnv), loc)

    }
    override fun twist(): Twist =
        Twist.obj(
            "Param::TypedId",
            Twist.attr("name", name.repr),
            Twist.value("type", type)
        )

}