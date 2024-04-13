package org.goodmath.pica.util

class Symbol private constructor(val repr: String) {
    override fun toString(): String {
        return "$<$repr>"
    }

    companion object {
        private val symbols = HashMap<String, Symbol>()
        fun fromString(s: String): Symbol {
            return symbols.getOrPut(s) { Symbol(s) }
        }
    }
}