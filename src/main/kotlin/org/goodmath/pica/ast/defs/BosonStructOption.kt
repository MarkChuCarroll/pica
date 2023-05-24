package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.TypedIdentifier
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class BosonStructOption(
    name: Symbol,
    val fields: List<TypedIdentifier>,
    loc: Location,
    val boundFrom: BosonStructOption? = null
): BosonOption(name, loc) {
    override fun bind(typeEnv: Map<TypeVar, Type>): BosonOption {
        return BosonStructOption(name, fields.map { it ->
            TypedIdentifier(it.name, it.type.bind(typeEnv), it.loc)
        }, loc, this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Def::Boson::StructOption",
            Twist.attr("name", name.repr),
            Twist.arr("fields", fields)
        )
}