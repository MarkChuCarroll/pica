package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist

abstract class BosonPattern(val optionName: Symbol, loc: Location): AstNode(loc) {
}

class BosonPatternBinding(val fieldName: Symbol, val boundVariable: Symbol, loc: Location): AstNode(loc) {


    override fun twist(): Twist =
        Twist.obj(
            "Action::Receive::PatternBinding",
            Twist.attr("fieldName", fieldName.repr),
            Twist.attr("boundVariable", boundVariable.repr)
        )

}

class BosonStructPattern(
    optionName: Symbol,
    val bindings: List<BosonPatternBinding>,
    loc: Location
): BosonPattern(optionName, loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Action::Receive::BosonStructPattern",
            Twist.attr("optionName", optionName.repr),
            Twist.arr("bindings", bindings)
        )
}

class BosonTuplePattern(optionName: Symbol,
                        val bindings: List<Symbol>,
                        loc: Location
): BosonPattern(optionName, loc) {
    override fun twist(): Twist =
        Twist.obj("Action::Receive::BosonTuplePattern",
            Twist.attr("OptionName", optionName.repr),
            Twist.arr("fields", bindings.map { b -> Twist.attr("boundVariable", b.repr) }))
}