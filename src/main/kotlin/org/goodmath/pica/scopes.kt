package org.goodmath.pica

import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.defs.Definition
import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.defs.HadronDefinition
import org.goodmath.pica.util.Symbol
import org.goodmath.pica.util.twist.Twist
import org.goodmath.pica.util.twist.Twistable

abstract class Scope: Twistable {
    abstract fun getDefinition(name: Identifier): Definition?


}

object RootScope: Scope() {
    private val defs = HashMap<Symbol, Definition>()
    private val scopes = HashMap<Identifier, HadronScope>()

    fun getHadronScope(id: Identifier): HadronScope? {
        return scopes[id]
    }

    override fun getDefinition(name: Identifier): Definition? {
        if (name.hadronId == null) {
            return defs[name.name]
        } else {
            return scopes[name.hadronId]?.getDefinition(name)
        }
    }

    override fun twist(): Twist =
        Twist.obj("RootScope",
            Twist.arr("Hadrons",
                scopes.values.toList()
            ),
            Twist.arr("DefinedNames",
                defs.values.toList()
            )
        )

    fun setHadronScope(id: Identifier, scope: HadronScope) {
        this.scopes[id] = scope
    }

}

class HadronScope(val id: Identifier, val hadron: HadronDefinition): Scope() {
    val defs = HashMap<Symbol, Definition>()
    val imports = HashMap<Symbol, Identifier>()

    init {
        // Add the imports to the namespace.
        for (imp in hadron.imports) {
            for (name in imp.names) {
                imports[name] = Identifier(hadron.id, name, imp.loc)
            }
        }
        for (def in hadron.defs) {
            defs.put(def.name, def)
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


    override fun twist(): Twist =
        Twist.obj("Scope::Hadron",
            Twist.arr("Imports",
                imports.values.toList()
            ),
            Twist.arr("Definitions",
                defs.values.toList()
            )
        )

    private fun addImportedId(name: Symbol, referencedId: Identifier) {
        imports[name] = referencedId
    }

}

class LocalScope(val parent: Scope, val origin: AstNode): Scope() {
    val definitions = HashMap<Symbol, Definition>()

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


    override fun twist(): Twist =
        Twist.leaf("LocalScope")
}
