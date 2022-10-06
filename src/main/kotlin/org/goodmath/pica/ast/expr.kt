/* Copyright 2022, Mark C. Chu-Carroll
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.goodmath.pica.ast

import org.goodmath.pica.util.Twist

abstract class Expr(loc: Location) : KAstNode(loc)

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

class BosonStructExpr(
    val bosonOptionName: Identifier,
    val fields: List<Pair<String, Expr>>,
    loc: Location
) : Expr(loc) {
    override fun twist(): Twist =
        Twist.obj("Expr::Boson::Struct",
            Twist.attr("name", bosonOptionName.toString()),
            Twist.arr("fields",
                fields.map { (name, value) ->
                    Twist.obj("field",
                        Twist.attr("name", name),
                        Twist.value("value", value))}))
}

class BosonTupleExpr(val bosonOptionName: Identifier, val fields: List<Expr>, loc: Location) : Expr(loc) {
    override fun twist(): Twist =
        Twist.obj("Expr::Boson::Tuple",
            Twist.attr("name", bosonOptionName.toString()),
            Twist.arr("fields", fields)
        )
}

class CreateQuarkExpr(
    val quarkType: SType,
    val valueArgs: List<Expr>,
    loc: Location
) : Expr(loc) {
    override fun twist(): Twist =
        Twist.obj("Expr::CreateQuark",
            Twist.value("type", quarkType),
            Twist.arr("valueArgs", valueArgs)
        )

}

class NewChannelExpr(val type: SType, loc: Location) : Expr(loc) {
    override fun twist(): Twist =
        Twist.obj("Expr::NewChannel",
            Twist.value(
                "type", type
            ))

}

class NarrowChannelToInExpr(val chan: Expr, loc:Location): Expr(loc) {
    override fun twist(): Twist =
        Twist.obj("Expr::NarrowToIn",
            Twist.value("channel", chan))
}

class NarrowChannelToOutExpr(val chan: Expr, loc:Location): Expr(loc) {
    override fun twist(): Twist =
        Twist.obj("Expr::NarrowToOut",
            Twist.value("channel", chan))
}


class ListExpr(val type: SType, val values: List<Expr>, loc: Location) : Expr(loc) {
    override fun twist(): Twist =
        Twist.obj("Expr::List",
            Twist.arr("values", values)
            )
}

enum class LiteralKind {
    StrLit, IntLit, FloatLit, CharLit, SymLit
}

class LiteralExpr(val kind: LiteralKind, val value: Any, loc: Location) : Expr(loc) {
    override fun twist(): Twist =
        Twist.obj("Expr::Literal",
            Twist.attr("kind", kind.toString()),
            Twist.attr("value", value.toString()))
}

abstract class LValExpr(loc: Location) : Expr(loc)

class IdLValueExpr(val id: Identifier, loc: Location) : LValExpr(loc) {
    override fun twist(): Twist =
        Twist.obj("Expr::LVal::Id",
            Twist.attr("id", id.toString())
        )

}

class FieldLValueExpr(val baseLValue: LValExpr, val field: String, loc: Location) : LValExpr(loc) {
    override fun twist(): Twist =
        Twist.obj("Expr::LVal::Field",
            Twist.value("base", baseLValue),
            Twist.attr("field", field))
}

class IndexedLValueExpr(val baseLValue: LValExpr, val idx: Int, loc: Location) : LValExpr(loc) {
    override fun twist(): Twist =
        Twist.obj("Expr::LVal::Field",
            Twist.value("base", baseLValue),
            Twist.attr("index", idx.toString()))
}
