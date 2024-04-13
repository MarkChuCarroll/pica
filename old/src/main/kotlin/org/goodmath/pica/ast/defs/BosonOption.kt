package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.Type
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol

abstract class BosonOption(
    val name: Symbol,
    loc: Location
): AstNode(loc) {
    lateinit var boson: BosonDefinition

    abstract fun isFullyConcrete(): Boolean

    fun setBosonDefinition(b: BosonDefinition) {
        this.boson = b
    }

    abstract fun bind(typeEnv: Map<TypeVar, Type>): BosonOption
}