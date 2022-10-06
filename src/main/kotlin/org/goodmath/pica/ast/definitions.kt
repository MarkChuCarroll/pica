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

abstract class Definition(
    val module: Identifier,
    val name: String,
    val typeParams: List<TypeVar>?,
    loc: Location): KAstNode(loc)

class BosonDefinition(
    module: Identifier,
    name: String,
    typeParams: List<TypeVar>?,
    val options: List<BosonOption>,
    loc: Location
): Definition(module, name, typeParams, loc) {
    override fun twist(): Twist =
        Twist.obj("Definition::Boson",
            Twist.attr("module", module.toString()),
            Twist.attr("name", name),
            Twist.opt(typeParams?.let { Twist.arr("typeParams", it) }),
            Twist.arr("options",
                options))
}

abstract class BosonOption(val name: String, loc: Location): KAstNode(loc) {
}

class BosonStructOption(
    name: String,
    val fields: List<Pair<String, SType>>,
    loc: Location
): BosonOption(name, loc) {
    override fun twist(): Twist =
        Twist.obj("Boson::StructOption",
            Twist.attr("name", name),
            Twist.arr("fields",
                fields.map { f ->
                    Twist.obj("BosonStructField",
                        Twist.attr("name", f.first),
                        Twist.value("type", f.second))}))
}

class BosonTupleOption(
    name: String,
    val fields: List<SType>,
    loc: Location
): BosonOption(name, loc) {
    override fun twist(): Twist =
        Twist.obj("Boson::TupleOption",
            Twist.attr("name", name),
            Twist.arr("fields", fields))

}

class QuarkDefinition(
    module: Identifier,
    name: String,
    typeParams: List<TypeVar>?,
    val valueParams: List<Pair<String, SType>>,
    val composes: List<SType>,
    val channels: List<ChannelDef>,
    val slots: List<SlotDef>,
    val actions: Action,
    loc: Location
): Definition(module, name, typeParams, loc) {
    override fun twist(): Twist =
        Twist.obj("Definition::Quark",
            Twist.attr("module", module.toString()),
            Twist.attr("name", name),
            Twist.opt(typeParams?.let { Twist.arr("typeParams", it)}),
            Twist.arr("valueParams",
                valueParams.map { (n, t) -> Twist.obj("param",
                Twist.attr("name", n),
                Twist.value("type", t)) }),
            Twist.arr("channels", channels),
            Twist.arr("slots", slots),
            Twist.arr("actions", actions))
}

class ChannelDef(
    val name: String,
    val msgType: SType,
    loc: Location
): KAstNode(loc) {
    override fun twist(): Twist =
        Twist.obj("Definition::Channel",
            Twist.attr("name", name),
            Twist.value("messageType", msgType))

}

class SlotDef(
    val name: String,
    val type: SType,
    val initValue: Expr,
    loc: Location
): KAstNode(loc) {
    override fun twist(): Twist =
        Twist.obj("Object::Slot",
            Twist.attr("name", name),
            Twist.value("type", type),
            Twist.value("initValue", initValue))

}

class FlavorDef(module: Identifier, name: String, typeParams: List<TypeVar>?,
                val channels: List<ChannelDef>, loc: Location): Definition(module, name, typeParams, loc) {
    override fun twist(): Twist =
        Twist.obj("Definition::Flavor",
            Twist.attr("module", module.toString()),
            Twist.attr("name", name),
            Twist.opt(typeParams?.let { Twist.arr("typeParams", it)}),
            Twist.arr("channels", channels))

}
