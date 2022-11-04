package org.goodmath.pica.ast.expr

import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.twist.Twist

enum class Operator {
    And, Or, Eq, NotEq, Greater, GreaterEq, Less, LessEq, Plus, Minus, Times, Div, Modulo;

    companion object {
        val ops: Map<String, Operator> = mapOf(
            "and" to And,
            "or" to Or,
            "==" to Eq,
            "!=" to NotEq,
            ">" to Greater,
            ">=" to GreaterEq,
            "<" to Less,
            "<=" to LessEq,
            "+" to Plus,
            "-" to Minus,
            "*" to Times,
            "/" to Div,
            "%" to Modulo
        )

        fun fromString(opStr: String): Operator {
            return ops[opStr] ?: throw RuntimeException("Unknown operator: '$opStr'")
        }
    }
}

class OperatorExpr(val op: Operator, val operands: List<Expr>, loc: Location) : Expr(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Expr::Operator",
            Twist.attr("operator", op.toString()),
            Twist.arr("operands", operands)
        )
}