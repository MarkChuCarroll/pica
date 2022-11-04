package org.goodmath.pica.ast.types

import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.twist.*

class ChannelType(val direction: Dir, val bosonType: SType, loc: Location): SType(emptyList(), loc) {
    enum class Dir { In, Out, Both }

    override fun twist(): Twist =
        obj(
            "Type::Channel",
            attr("direction", direction.toString()),
            value("bosonType", bosonType)
        )

}