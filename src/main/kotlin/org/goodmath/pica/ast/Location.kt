package org.goodmath.pica.ast

data class Location(val source: String, val line: Int, val column: Int) {
    companion object {
        val None = Location("", 0, 0)
    }
}