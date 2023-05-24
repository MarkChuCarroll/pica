package org.goodmath.pica.ast.types

import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.twist.*

class ChannelType(val direction: Dir, val bosonType: Type, loc: Location,
    override val boundFrom: ChannelType? = null): Type(loc) {
    enum class Dir { In, Out, Both }

    override fun bind(bindings: Map<TypeVar, Type>): ChannelType {
        return ChannelType(direction, bosonType.bind(bindings), loc)
    }

    override fun isSatisfiedBy(candidateType: Type): Boolean {
        TODO("Not yet implemented")
    }

    override fun twist(): Twist =
        obj(
            "Type::Channel",
            attr("direction", direction.toString()),
            value("bosonType", bosonType)
        )

}