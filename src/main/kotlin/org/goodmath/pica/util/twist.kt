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
package org.goodmath.pica.util

fun StringBuilder.indent(i: Int) {
    append("   ".repeat(i))
}

interface Twistable {
    fun twist(): Twist
}

sealed class Twist: Twistable {
    override fun toString(): String {
        val sb = StringBuilder()
        this.render(sb, 0)
        return sb.toString()
    }



    override fun twist(): Twist = this
    abstract fun render(sb: StringBuilder, indent: Int)

    abstract fun renderCode(i: Int): String
    companion object {
        fun obj(name: String, vararg children: Twistable): Twist {
            return ObjectNode(name, children.toList())
        }

        fun obj(name: String, children: List<Twistable>): Twist {
            return ObjectNode(name, children)
        }

        fun arr(name: String, vararg children: Twistable): Twist {
            return ArrayNode(name, children.toList())
        }

        fun arr(name: String, children: List<Twistable>): Twist {
            return ArrayNode(name, children)
        }

        fun attr(name: String, value: String): Twist {
            return AttributeNode(name, value)
        }

        fun value(name: String, value: Twistable): Twist {
            return ValueNode(name, value)
        }

        fun leaf(value: String): Twist {
            return LeafNode(value)
        }

        fun opt(t: Twistable?): Twist {
            return OptNode(t)
        }
    }
}

class ObjectNode(val name: String, val children: List<Twistable>): Twist() {
    override fun render(sb: StringBuilder, indent: Int) {
        sb.indent(indent)
        sb.append("obj $name:\n")
        for (c in children) {
            c.twist().render(sb, indent + 1)
        }
    }

    override fun renderCode(i: Int): String =
        "  ".repeat(i) +
                "Twist.obj(\"${name}\",\n" +
                children.map { it.twist().renderCode(i+1) }.joinToString("") +
                "  ".repeat(i) + ")\n"
}

class ArrayNode(val name: String, val children: List<Twistable>): Twist() {
    override fun render(sb: StringBuilder, indent: Int) {
        if (children.isNotEmpty()) {
            sb.indent(indent)
            sb.append("$name = array:\n")
            for (c in children) {
                c.twist().render(sb, indent + 1)
            }
        }
    }

    override fun renderCode(i: Int): String {
        return if (children.isNotEmpty()) {
            "  ".repeat(i) +
                    "Twist.arr(\"${name}\",\n" +
                    children.map { it.twist().renderCode(i + 1) }.joinToString("") +
                    "  ".repeat(i) + ")\n"
        } else {
            ""
        }
    }
}

class AttributeNode(val name: String, val value: String): Twist() {
    override fun render(sb: StringBuilder, indent: Int) {
        sb.indent(indent)
        sb.append("$name = $value\n")
    }

    override fun renderCode(i: Int): String {
        return "  ".repeat(i) + "Twist.attr(\"$name\", \"$value\"),\n"
    }
}

class ValueNode(val name: String, val value: Twistable): Twist(){
    override fun render(sb: StringBuilder, indent: Int) {
        sb.indent(indent)
        sb.append("$name:\n")
        value.twist().render(sb, indent+1)
    }

    override fun renderCode(i: Int): String {
        return "  ".repeat(i) + "Twist.value(\"$name\",\n" + value.twist().renderCode(i+1) +
                "  ".repeat(i) +
                ")\n"
    }
}

class LeafNode(val name: String): Twist() {
    override fun render(sb: StringBuilder, indent: Int) {
        sb.indent(indent)
        sb.append(name).append("\n")
    }

    override fun renderCode(i: Int): String {
        return "  ".repeat(i) + "Twist.leaf(\"$name\")\n"
    }
}

class OptNode(val t: Twistable?): Twist() {
    override fun render(sb: StringBuilder, indent: Int) {
        t?.twist()?.render(sb, indent)
    }

    override fun renderCode(i: Int): String {
        return t?.twist()?.renderCode(i) ?: ""
    }

}