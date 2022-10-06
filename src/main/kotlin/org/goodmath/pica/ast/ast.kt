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
import org.goodmath.pica.util.Twistable

data class Location(val source: String, val line: Int, val column: Int) {
    companion object {
        val None = Location("", 0, 0)
    }
}

abstract class KAstNode(val loc: Location): Twistable {
}

class Identifier(val module: Identifier?, val name: String, loc: Location): KAstNode(loc) {
    override fun twist(): Twist =
        if (module == null) {
            Twist.obj(
                "Ident",
                Twist.attr("name", name))
        } else{
            Twist.obj(
                "Ident",
                Twist.value("module", module),
                Twist.attr("name", name)
            )
    }

    override fun toString(): String {
        return if (module != null) {
            "$module::$name"
        } else {
            name
        }
    }

    companion object {
        fun fromList(parts: List<String>, loc: Location): Identifier {
            var id: Identifier? = null
            for (p in parts) {
                id = Identifier(id, p, loc)
            }
            return id!!
        }

        fun fromString(id: String, loc: Location): Identifier {
            return fromList(id.split("::"), loc)
        }
    }
}

class UseDef(val moduleId: Identifier, val names: List<String>, loc: Location): KAstNode(loc) {
    override fun twist(): Twist =
        Twist.obj(
            "Use",
            Twist.attr("module", moduleId.toString()),
            Twist.arr("names", names.map { Twist.leaf(it) })
        )
}

class PicaModule(val id: Identifier, val imports: List<UseDef>, val defs: List<Definition>):
        KAstNode(Location(id.toString(), 0, 0)) {
    override fun twist(): Twist =
        Twist.obj("PicaModule",
            Twist.attr("id", id.toString()),
            Twist.arr("uses", imports),
            Twist.arr("defs", defs))
}

