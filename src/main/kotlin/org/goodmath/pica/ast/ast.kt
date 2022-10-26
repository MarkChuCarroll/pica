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
import org.goodmath.pica.util.Twistable

data class Location(val source: String, val line: Int, val column: Int) {
    companion object {
        val None = Location("", 0, 0)
    }
}

abstract class AstNode(val loc: Location): Twistable {
}

class Identifier(val hadronId: Identifier?, val name: Symbol, loc: Location): AstNode(loc) {
    override fun twist(): Twist =
        if (hadronId == null) {
            Twist.obj(
                "Ident",
                Twist.attr("name", name.repr))
        } else {
            Twist.obj(
                "Ident",
                Twist.value("hadron", hadronId),
                Twist.attr("name", name.repr))

    }

    override fun toString(): String {
        return if (hadronId != null) {
            "$hadronId::${name.repr}"
        } else {
            name.repr
        }
    }

    companion object {
        fun fromList(parts: List<String>, loc: Location): Identifier {
            var id: Identifier? = null
            for (p in parts) {
                id = Identifier(id, Symbol.fromString(p), loc)
            }
            return id!!
        }

        fun fromString(id: String, loc: Location): Identifier {
            return fromList(id.split("::"), loc)
        }
    }
}

class UseDef(val hadronId: Identifier, val names: List<Symbol>, loc: Location): AstNode(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Use",
            Twist.attr("hadron", hadronId.toString()),
            Twist.arr("names", names.map { Twist.leaf(it.repr) })
        )
}

class HadronDefinition(val id: Identifier, val imports: List<UseDef>, val defs: List<Definition>):
        AstNode(Location(id.toString(), 0, 0)) {
    override fun twist(): Twist =
        Twist.obj("HadronDefinition",
            Twist.attr("id", id.toString()),
            Twist.arr("uses", imports),
            Twist.arr("defs", defs))
}

