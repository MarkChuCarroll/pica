package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class FlavorDef(
    hadronId: Identifier,
    name: Symbol,
    typeParams: List<TypeVar>,
    val composes: List<Type>,
    val channels: List<ChannelDef>,
    loc: Location,
    val boundFrom: FlavorDef? = null
): Definition(hadronId, name, typeParams, loc) {
    override fun instantiate(typeArgs: List<Type>): Definition {
        validateTypeParameters(typeArgs)
        validateTypeParameters(typeArgs)
        val typeEnv = typeParams.zip(typeArgs).associate { (typeVar, concreteType) -> typeVar to concreteType}
        return FlavorDef(hadronId, name, emptyList(),
            composes.map { it.bind(typeEnv) }, channels.map { it.bind(typeEnv) }, loc,
            this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Def::Flavor",
            Twist.attr("hadron", hadronId.toString()),
            Twist.attr("name", name.repr),
            Twist.arr("typeParams", typeParams),
            Twist.arr("composes", composes),
            Twist.arr("channels", channels)
        )

}