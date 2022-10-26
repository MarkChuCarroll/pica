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


// SType => Syntactic Type
abstract class SType(val typeArgs: List<SType>?, loc: Location): AstNode(loc) {
}

class NamedType(val typeId: Identifier, typeArgs: List<SType>?, loc: Location): SType(typeArgs, loc) {
    override fun twist(): Twist =
        Twist.obj("Type::Named",
            Twist.attr("id", typeId.toString()),
            Twist.opt(typeArgs?.let { Twist.arr("typeArgs", it) }))
}

class TypeVar(val name: Symbol,
              val constraint: List<SType>?,
              loc: Location): SType(null, loc) {

    override fun twist(): Twist =
        Twist.obj("Type::TypeVar",
            Twist.attr("name", name.repr),
            Twist.opt(constraint?.let { Twist.arr("constraint", it)}))

}
