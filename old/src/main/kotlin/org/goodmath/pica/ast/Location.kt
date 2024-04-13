package org.goodmath.pica.ast

sealed class Location

data class SourceLocation(val hadronId: Identifier, val line: Int, val column: Int): Location() {
}

data class SystemLocation(val descriptor: String): Location()