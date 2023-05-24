package org.goodmath.pica.ast.types

import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class TypeVar(val name: Symbol,
              val constraint: List<Type>?,
              loc: Location,
              override val boundFrom: TypeVar? = null
): Type(loc) {
    override fun bind(bindings: Map<TypeVar, Type>): Type {
        return TypeVar(name, constraint?.map { it.bind(bindings)}, loc, this)
    }

    override fun isSatisfiedBy(candidateType: Type): Boolean {
        TODO("Not yet implemented")
    }

    override fun twist(): Twist =
        Twist.obj("Type::TypeVar",
            Twist.attr("name", name.repr),
            Twist.opt(constraint?.let { Twist.arr("constraint", it) })
        )

}