package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

class BosonDefinition(
    hadronId: Identifier,
    name: Symbol,
    typeParams: List<TypeVar>,
    val options: List<BosonOption>,
    loc: Location,
    val instantiatedFrom: BosonDefinition? = null
): Definition(hadronId, name, typeParams, loc) {
    override fun instantiate(typeArgs: List<Type>): Definition {
        validateTypeParameters(typeArgs)
        val typeEnv = typeParams.zip(typeArgs).associate { (typeVar, concreteType) -> typeVar to concreteType}
        return BosonDefinition(hadronId, name, emptyList(), options.map { it -> it.bind(typeEnv) }, loc,
            this)
    }

    override fun twist(): Twist =
        Twist.obj(
            "Def::Boson",
            Twist.attr("hadron", hadronId.toString()),
            Twist.attr("name", name.repr),
            Twist.arr("typeParams", typeParams),
            Twist.arr(
                "options",
                options
            )
        )

}