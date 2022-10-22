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

    abstract fun setType(name: Symbol, type: ConcreteType)

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

    override fun setType(name: Symbol, type: ConcreteType) {
        types.put(name, type)
    }

    fun getHadronScope(name: Identifier): HadronScope? {
        return scopes[name]
    }

    fun setHadronScope(name: Identifier, scope: HadronScope) {
        scopes[name] = scope
    }

}

class HadronScope(val id: Identifier, val hadron: HadronDefinition): Scope() {
    val defs = HashMap<Symbol, Definition>()
    val types = HashMap<Symbol, ConcreteType>()
    val imports = HashMap<Symbol, Identifier>()

    init {
        // Add the imports to the namespace.
        for (imp in hadron.imports) {
            for (name in imp.names) {
                imports[name] = Identifier(hadron.id, name, imp.loc)
            }
        }
    }

    override fun getDefinition(name: Identifier): Definition? {
        return when (name.hadronId) {
            null -> {
                imports[name.name]
                    ?.let { getDefinition(it) }
                    ?: defs[name.name]
            }
            id -> {
                defs[name.name]
            }
            else -> {
                RootScope.getDefinition(name)
            }
        }
    }

    override fun getType(name: Identifier): ConcreteType? {
        return when (name.hadronId) {
            null -> {
                imports[name.name]?.let { getType(it) }
                    ?: types[name.name] ?: RootScope.getType(name)
            }
            id -> {
                types[name.name]
            }
            else -> {
                RootScope.getType(name)
            }
        }

    }

    override fun setType(name: Symbol, type: ConcreteType) {
        types[name] = type
    }

    private fun addImportedId(name: Symbol, referencedId: Identifier) {
        imports[name] = referencedId
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

    override fun setType(name: Symbol, type: ConcreteType) {
        types.put(name, type)
    }


}