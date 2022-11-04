package org.goodmath.pica.ast.types

import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class TypeVar(val name: Symbol,
              val constraint: List<SType>?,
              loc: Location
): SType(emptyList(), loc) {

    override fun twist(): Twist =
        Twist.obj("Type::TypeVar",
            Twist.attr("name", name.repr),
            Twist.opt(constraint?.let { Twist.arr("constraint", it) })
        )

}