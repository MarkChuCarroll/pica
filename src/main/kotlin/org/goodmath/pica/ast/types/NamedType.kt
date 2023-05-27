package org.goodmath.pica.ast.types

import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.twist.Twist

class NamedType(val typeId: Identifier, val typeArgs: List<Type>, loc: Location,
    override val boundFrom: NamedType? = null): Type(loc) {
    override fun bind(bindings: Map<TypeVar, Type>): Type {
        return NamedType(typeId, typeArgs.map { it.bind(bindings) }, loc,
            this)
    }

    override fun isSatisfiedBy(candidateType: Type): Boolean {
        TODO("Not yet implemented")
    }

    override fun isFullyConcrete(): Boolean {
        return typeArgs.all { it.isFullyConcrete() }
    }

    override fun twist(): Twist =
        Twist.obj("Type::Named",
            Twist.attr("id", typeId.toString()),
                  Twist.arr("typeArgs", typeArgs)
        )


}