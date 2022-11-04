package org.goodmath.pica.ast

import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class Identifier(val hadronId: Identifier?, val name: Symbol, loc: Location): AstNode(loc) {
    override fun twist(): Twist =
        if (hadronId == null) {
            Twist.obj(
                "Ident",
                Twist.attr("name", name.repr)
            )
        } else {
            Twist.obj(
                "Ident",
                Twist.value("hadron", hadronId),
                Twist.attr("name", name.repr)
            )

    }

    override fun toString(): String {
        return if (hadronId != null) {
            "$hadronId::${name.repr}"
        } else {
            name.repr
        }
    }

    companion object {
        fun fromList(parts: List<String>, loc: Location): Identifier {
            var id: Identifier? = null
            for (p in parts) {
                id = Identifier(id, Symbol.fromString(p), loc)
            }
            return id!!
        }

        fun fromString(id: String, loc: Location): Identifier {
            return fromList(id.split("::"), loc)
        }
    }
}