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

    init {
        channels.map { it.setOuterDefinition(this) }
    }

    override fun isFullyConcrete(): Boolean {
        return composes.all { it.isFullyConcrete() } &&
                channels.all { it.type.isFullyConcrete() }
    }

    override fun instantiate(typeArgs: List<Type>): Definition {
        validateTypeParameters(typeArgs)
        return registry.getOrCreateInstantiation(this, typeArgs)
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

    companion object {
        private fun instantiator(flavor: FlavorDef, instantiationArguments: List<Type>): FlavorDef {
            val typeEnv = flavor.typeParams.zip(instantiationArguments).associate { (typeVar, concreteType) -> typeVar to concreteType}
            return FlavorDef(flavor.hadronId, flavor.name, emptyList(),
                flavor.composes.map { it.bind(typeEnv) }, flavor.channels.map { it.bind(typeEnv) }, flavor.loc,
                flavor)

        }
        val registry = InstantiationRegistry(this::instantiator)
    }

}