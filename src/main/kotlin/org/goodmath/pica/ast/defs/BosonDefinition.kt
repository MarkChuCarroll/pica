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

    init {
        options.forEach { it.setBosonDefinition(this) }
    }

    override fun isFullyConcrete(): Boolean {
        return typeParams.isEmpty() && options.all { it.isFullyConcrete() }
    }

    override fun instantiate(typeArgs: List<Type>): Definition {
        validateTypeParameters(typeArgs)
        return registry.getOrCreateInstantiation(this, typeArgs)
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
    companion object {

        private fun instantiator(boson: BosonDefinition, instantiatorParameters: List<Type>): BosonDefinition {
            val typeEnv = boson.typeParams.zip(instantiatorParameters).associate { (typeVar, concreteType) -> typeVar to concreteType}
            return BosonDefinition(
                boson.hadronId,
                boson.name,
                emptyList(),
                boson.options.map { it -> it.bind(typeEnv) },
                boson.loc,
                boson)
        }

        val registry = InstantiationRegistry(this::instantiator)
    }

}