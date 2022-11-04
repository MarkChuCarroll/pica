package org.goodmath.pica.analysis

import org.goodmath.pica.ast.defs.Definition
import org.goodmath.pica.ast.types.ChannelType
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.*

/*
I've been spending a ton of time trying to figure out how to do type analysis - where's the line
between concrete types and syntactic types, and how does the type analyzer actually work.

Then last night, I realized:
- Syntactic types are applied to named entities in the program. You can think of them
  as being attached to the identifier. You look them up by looking up the type of
  the identifier associated with the entity. You get the syntactic type of a quark
  by looking up the type associated with the quark name, and what you get back is the
  instantiable abstract type of a quark.
- Type analysis is focused narrowly on expressions and the ways that actions/statements
  use those expressions. The actions don't have types, but they do introduce constraints
  on the types of the expressions that they use. For example, `send x(y)`
  requires that x is a channel type `[T]chan T`, and y is of the associated boson type,
  `T`.
\


 */

abstract class ConcreteType(): Twistable {
    abstract fun bind(bindings: Map<ConcreteTypeVariable, ConcreteType>): ConcreteType

}

class ConcreteDefinedType(val def: Definition, val typeArgs: List<ConcreteType>): ConcreteType() {
    override fun bind(bindings: Map<ConcreteTypeVariable, ConcreteType>): ConcreteType {
        val boundTypeArgs = typeArgs.map { it.bind(bindings) }
        return if (typeArgs != boundTypeArgs) {
            ConcreteDefinedType(def, boundTypeArgs)
        } else {
            this
        }
    }

    override fun twist(): Twist =
        obj("ConcreteType::Defined",
            value("def", def),
            arr("typeArgs", typeArgs)
        )
}

class ConcreteTypeVariable(val tv: TypeVar): ConcreteType() {
    val id = newId(tv.name)
    override fun bind(bindings: Map<ConcreteTypeVariable, ConcreteType>): ConcreteType {
        return bindings[this] ?: this
    }

    override fun twist(): Twist =
        obj("ConcreteType::TypeVar",
            attr("id", id.toString()))

    companion object {
        var idx = 0

        fun newId(name: Symbol): Symbol {
            idx++
            return Symbol.fromString("$name<$idx>")
        }
    }
}

class ConcreteChannelType(val sType: ChannelType): ConcreteType() {
    val direction = sType.direction
    val bosonType = TypeRegistry.getConcreteType(sType)
    override fun bind(bindings: Map<ConcreteTypeVariable, ConcreteType>): ConcreteType {
        TODO()
    }

    override fun twist(): Twist =
        obj("ConcreteType::Channel",
            attr("direction", direction.toString()),
            value("bosonType", bosonType))

}