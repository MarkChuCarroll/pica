package org.goodmath.pica.analysis

import org.goodmath.pica.ast.TypedIdentifier
import org.goodmath.pica.ast.action.Action
import org.goodmath.pica.ast.defs.ChannelDef
import org.goodmath.pica.ast.defs.QuarkBehavior
import org.goodmath.pica.ast.defs.QuarkDefinition
import org.goodmath.pica.ast.defs.SlotDef
import org.goodmath.pica.ast.expr.Expr
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar

class AnalyzedQuark(val def: QuarkDefinition, val typeArgs: List<Type>) {
    /**
     * When a quark is instantiated, we need to:
     * (1) Create a type environment, which will be used to instantiate
     *   its body. This means that we need to create a map from the
     *   unique names of its type parameters to the actual concrete
     *   types used to instantiate them.
     * (2) Instantiate its slots, behaviors, and channels - binding
     *   any references to the type parameters according to the
     *   type environment.
     *
     * Instantiating a slot means associating a concrete slot type
     * with the parametric slot type in the definition. The result will
     * be a map from SlotDefinition to ConcreteType.
     *
     * Instantiating a behavior means associating a concrete type
     * with each of its parameters, by binding any references
     * to the type parameters, and then instantiating each
     * of the actions in its body. The result will be a mapping from
     * the behavior name to an object representing the instantiated
     * behavior. This will, in turn, contain a mapping from each
     * parameter to a concrete type, and for each action, a mapping
     * from the action to an instantiated action.
     *
     * Instantiating an action means computing the type of each
     * of its expressions, and binding any references to the type
     * parameters in those types. The result is a mapping from
     * each expression (and subexpression) in the action to a
     * concrete type.
     */

    val instantiatedChannels: Map<ChannelDef, InstantiatedChannel> = TODO()

    fun instantiate(bindings: Map<TypeVar, Type>): AnalyzedQuark {
        val boundTypeArgs = typeArgs.map { it.bind(bindings) }
        return AnalyzedQuark(def, boundTypeArgs)
    }

    fun check() {

    }


}

class InstantiatedChannel(
    val channelDef: ChannelDef,
    val concreteType: Type)

class InstantiatedSlot(
    val slotDef: SlotDef,
    val concreteType: Type
)

class InstantiatedAction(
    val action: Action,
    val types: Map<Expr, Type>
)

class InstantiatedBehavior(
    val behavior: QuarkBehavior,
    val expectedParameterTypes: Map<TypedIdentifier, Type>,
    val types: Map<Action, InstantiatedAction>
)
