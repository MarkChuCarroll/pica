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

abstract class Action(loc: Location): KAstNode(loc)


class AssignmentAction(val target: LValExpr, val value: Expr, loc: Location): Action(loc) {
    override fun twist(): Twist =
        Twist.obj("Action::Assignment",
            Twist.value("target", target),
            Twist.value("value", value))

}

class ChoiceAction(val choices: List<Action>, loc: Location): Action(loc) {
    override fun twist(): Twist =
        Twist.obj("Action::Choice",
            Twist.arr("choices", choices))

}

class ExitAction(loc: Location): Action(loc) {
    override fun twist(): Twist =
        Twist.leaf("Action::Exit")
}

class ForAction(val index: String, val collection: Expr, val action: Action, loc: Location): Action(loc) {
    override fun twist(): Twist =
        Twist.obj("Action::ForLoop",
            Twist.attr("index", index),
            Twist.value("collection", collection),
            Twist.value("action", action)
        )
}

class IfAction(val cond: Expr, val trueSide: Action, val falseSide: Action, loc: Location): Action(loc) {
    override fun twist(): Twist =
        Twist.obj("Action::If",
            Twist.value("cond", cond),
            Twist.value("true", trueSide),
            Twist.value("false", falseSide))
}

class LoopAction(val action: Action, loc: Location): Action(loc) {
    override fun twist(): Twist =
        Twist.obj("Action::Loop",
            Twist.value("action", action)
            )

}

class ParAction(val actions: List<Action>, loc: Location): Action(loc) {
    override fun twist(): Twist =
        Twist.obj("Action::Par",
            Twist.arr("actions", actions))
}

abstract class BosonPattern(val optionName: String, loc: Location): KAstNode(loc) {
}

class BosonStructPattern(
    optionName: String,
    val bindings: List<Pair<String, String>>,
    loc: Location): BosonPattern(optionName, loc) {
    override fun twist(): Twist =
        Twist.obj("BosonStructPattern",
            Twist.attr("optionName", optionName),
            Twist.arr("bindings",
                bindings.map { b -> Twist.obj("StructFieldBinding",
                    Twist.attr("structField", b.first),
                    Twist.attr("boundVariable", b.second)) }))
}

class BosonTuplePattern(optionName: String,
                        val bindings: List<String>,
                        loc: Location): BosonPattern(optionName, loc) {
    override fun twist(): Twist =
        Twist.obj("BosonTuplePattern",
        Twist.attr("OptionName", optionName),
            Twist.arr("fields", bindings.map { b -> Twist.attr("boundVariable", b) }))

}

class ReceiveAction(val channel: Expr,
                    val actions: List<Pair<BosonPattern, Action>>,
                    loc: Location): Action(loc) {
    override fun twist(): Twist =
        Twist.obj("Action::Receive",
            Twist.value("channel", channel),
            Twist.arr("actions", actions.map { (pat, act) ->
                Twist.obj("Option",
                    Twist.value("pattern", pat),
                    Twist.value("action", act)) }))
}

class SendAction(val channel: Expr, val value: Expr, loc: Location): Action(loc) {
    override fun twist(): Twist =
        Twist.obj("Action::Send",
            Twist.value("channel", channel),
            Twist.value("message", value))
}

class SequenceAction(val actions: List<Action>, loc: Location): Action(loc) {
    override fun twist(): Twist =
        Twist.obj("Action::Seq",
            Twist.arr("actions", actions))
}

// Spawn starts an action that sends or receives a message; as soon as that
// message is paired with a sender/reciever, it starts another copy of its action.
class SpawnAction(val action: Action, loc: Location): Action(loc) {
    override fun twist(): Twist =
        Twist.obj("Action::Spawn",
            Twist.value("action", action))
}


class VarDefAction(val name: String, val type: SType, val value: Expr, loc: Location): Action(loc) {
    override fun twist(): Twist =
        Twist.obj("Action::VarDef",
            Twist.attr("name", name),
            Twist.value("type", type),
            Twist.value("value", value))

}

class WhileAction(val cond: Expr, val action: Action, loc: Location): Action(loc) {
    override fun twist(): Twist =
        Twist.obj("Action::While",
            Twist.value("cond", cond),
            Twist.value("action", action))
}



