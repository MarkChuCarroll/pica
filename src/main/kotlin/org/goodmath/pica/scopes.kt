package org.goodmath.pica

import org.goodmath.pica.analysis.ConcreteType
import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Definition
import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.HadronDefinition
import org.goodmath.pica.util.Symbol

abstract class Scope {
    abstract fun getDefinition(name: Identifier): Definition?
    abstract fun getType(name: Identifier): ConcreteType?
}

object RootScope: Scope() {
    private val defs = HashMap<Symbol, Definition>()
    private val types = HashMap<Symbol, ConcreteType>()
    private val scopes = HashMap<Identifier, HadronScope>()

    override fun getDefinition(name: Identifier): Definition? {
        if (name.hadronId == null) {
            return defs[name.name]
        } else {
            return scopes[name.hadronId]?.getDefinition(name)
        }
    }

    override fun getType(name: Identifier): ConcreteType? {
        if (name.hadronId == null) {
            return types[name.name]
        } else {
            return scopes[name.hadronId]?.getType(name)
        }
    }

    fun getHadronScope(name: Identifier): HadronScope? {
        return scopes[name]
    }

}

class HadronScope(val id: Identifier, val hadron: HadronDefinition): Scope() {
    val defs = HashMap<Symbol, Definition>()
    val types = HashMap<Symbol, ConcreteType>()

    override fun getDefinition(name: Identifier): Definition? {
        if (name.hadronId == null) {
            return defs[name.name]?:RootScope.getDefinition(name)
        } else if (name.hadronId == id) {
            return defs[name.name]
        } else {
            return RootScope.getDefinition(name)
        }
    }

    override fun getType(name: Identifier): ConcreteType? {
        if (name.hadronId == null) {
            return types[name.name]?:RootScope.getType(name)
        } else if (name.hadronId == id) {
            return types[name.name]
        } else {
            return RootScope.getType(name)
        }

    }
}

class LocalScope(val parent: Scope, val origin: AstNode): Scope() {
    val definitions = HashMap<Symbol, Definition>()
    val types = HashMap<Symbol, ConcreteType>()

    override fun getDefinition(name: Identifier): Definition? {
        return if (name.hadronId == null) {
            if (name.name in definitions) {
                definitions[name.name]
            } else {
                parent.getDefinition(name)
            }
        } else {
            parent.getDefinition(name)
        }
    }

    override fun getType(name: Identifier): ConcreteType? {
        return if (name.hadronId == null) {
            if (name.name in types) {
                types[name.name]
            } else {
                parent.getType(name)
            }
        } else {
            parent.getType(name)
        }
    }

}