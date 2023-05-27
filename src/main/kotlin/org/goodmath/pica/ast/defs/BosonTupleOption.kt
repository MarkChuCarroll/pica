package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class BosonTupleOption(
    name: Symbol,
    val fields: List<Type>,
    loc: Location,
    val boundFrom: BosonTuplePattern? = null
): BosonOption(name, loc) {
    override fun isFullyConcrete(): Boolean {
        return fields.all { it.isFullyConcrete() }
    }

    override fun bind(typeEnv: Map<TypeVar, Type>): BosonOption {
        return BosonTupleOption(name, fields.map { it.bind(typeEnv) }, loc, null)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Def::Boson::TupleOption",
            Twist.attr("name", name.repr),
            Twist.arr("fields", fields)
        )

}