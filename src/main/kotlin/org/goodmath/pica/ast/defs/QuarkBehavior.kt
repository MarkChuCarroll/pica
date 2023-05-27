package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.action.Action
import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.TypedIdentifier
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist
import org.goodmath.pica.util.twist.arr
import org.goodmath.pica.util.twist.obj

class QuarkBehavior(
    val name: Symbol,
    val args: List<TypedIdentifier>,
    val body: List<Action>,
    loc: Location,
    val boundFrom: QuarkBehavior? = null
): AstNode(loc) {

    lateinit var quarkDef: QuarkDefinition

    fun setQuarkDefinition(q: QuarkDefinition) {
        quarkDef = q
    }

    override fun twist(): Twist =
        obj(
            "Def::Quark::Behavior",
            arr("args", args),
            arr("body", body)
        )

    fun bind(bindings: Map<TypeVar, Type>): QuarkBehavior {
        val boundArgs = args.map { it ->
            TypedIdentifier(it.name, it.type.bind(bindings), it.loc)
        }
        val boundActions = body.map { it -> it.bind(bindings) }
        return QuarkBehavior(name, boundArgs, boundActions, loc, this)
    }

}