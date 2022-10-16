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

import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.Twist

abstract class Definition(
    val hadronId: Identifier,
    val name: Symbol,
    val typeParams: List<TypeVar>?,
    loc: Location): AstNode(loc) {
    val id = Identifier(hadronId, name, loc)
}

class BosonDefinition(
    hadronId: Identifier,
    name: Symbol,
    typeParams: List<TypeVar>?,
    val options: List<BosonOption>,
    loc: Location
): Definition(hadronId, name, typeParams, loc) {

    override fun twist(): Twist =
        Twist.obj("Def::Boson",
            Twist.attr("hadron", hadronId.toString()),
            Twist.attr("name", name.toString()),
            Twist.opt(typeParams?.let { Twist.arr("typeParams", it) }),
            Twist.arr("options",
                options))



}

abstract class BosonOption(
    val name: Symbol,
    loc: Location
): AstNode(loc)

class BosonStructOption(
    name: Symbol,
    val fields: List<TypedIdentifier>,
    loc: Location
): BosonOption(name, loc) {
    override fun twist(): Twist =
        Twist.obj("Def::Boson::StructOption",
            Twist.attr("name", name.toString()),
            Twist.arr("fields", fields))
}

class BosonTupleOption(
    name: Symbol,
    val fields: List<SType>,
    loc: Location
): BosonOption(name, loc) {
    override fun twist(): Twist =
        Twist.obj("Def::Boson::TupleOption",
            Twist.attr("name", name.toString()),
            Twist.arr("fields", fields))

}

class TypedIdentifier(
    val name: Symbol,
    val type: SType,
    loc: Location): AstNode(loc) {
    override fun twist(): Twist =
        Twist.obj("Param::TypedId",
            Twist.attr("name", name.toString()),
            Twist.value("type", type))

}

class QuarkDefinition(
    hadronId: Identifier,
    name: Symbol,
    typeParams: List<TypeVar>?,
    val valueParams: List<TypedIdentifier>,
    val composes: List<SType>,
    val channels: List<ChannelDef>,
    val slots: List<SlotDef>,
    val actions: Action,
    loc: Location
): Definition(hadronId, name, typeParams, loc) {
    override fun twist(): Twist =
        Twist.obj("Def::Quark",
            Twist.attr("hadron", hadronId.toString()),
            Twist.attr("name", name.toString()),
            Twist.opt(typeParams?.let { Twist.arr("typeParams", it)}),
            Twist.arr("valueParams", valueParams),
            Twist.arr("channels", channels),
            Twist.arr("slots", slots),
            Twist.arr("actions", actions))
}

class ChannelDef(
    val name: Symbol,
    val type: ChannelType,
    loc: Location
): AstNode(loc) {
    override fun twist(): Twist =
        Twist.obj("Definition::Channel",
            Twist.attr("name", name.toString()),
            Twist.value("type", type))

}

class SlotDef(
    val name: Symbol,
    val type: SType,
    val initValue: Expr,
    loc: Location
): AstNode(loc) {
    override fun twist(): Twist =
        Twist.obj("Def::Quark::Slot",
            Twist.attr("name", name.toString()),
            Twist.value("type", type),
            Twist.value("initValue", initValue))

}

class FlavorDef(
    hadronId: Identifier,
    name: Symbol,
    typeParams: List<TypeVar>?,
    val channels: List<ChannelDef>,
    loc: Location): Definition(hadronId, name, typeParams, loc) {
    override fun twist(): Twist =
        Twist.obj("Def::Flavor",
            Twist.attr("hadron", hadronId.toString()),
            Twist.attr("name", name.toString()),
            Twist.opt(typeParams?.let { Twist.arr("typeParams", it)}),
            Twist.arr("channels", channels))

}
