package org.goodmath.pica.util

class Symbol private constructor(val name: String) {
    override fun toString(): String {
        return "$<$name>"
    }

    companion object {
        private val symbols = HashMap<String, Symbol>()
        fun fromString(s: String): Symbol {
            return symbols.getOrPut(s) { Symbol(s) }
        }
    }
}