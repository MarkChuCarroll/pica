package org.goodmath.pica.util

import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Location

class PicaCompileError(val msg: String,
                       val loc: Location? = null,
                       val astNode: AstNode? = null,
                       val cause: Exception? = null) {
    override fun toString(): String {
        val result = StringBuilder()
        result.append("Error: $msg")
        if (loc != null) {
            result.append(" at $loc")
        }
        if (cause != null) {
            result.append(" caused by ").append(cause)
        }
        if (astNode != null) {
            result.append("\n")
            result.append("AST around error:\n")
            var astStr = astNode.twist().toString()
            if (astStr.length > 200) {
                astStr = astStr.substring(0, 100)
            }
            result.append(astStr)
        }
        result.append("\n")
        return result.toString()
    }
}

class PicaCompileException(val msg: String,
                           loc: Location? = null,
                           val astNode: AstNode? = null,
                           cause: Exception? = null): Exception(msg) {
}

object PicaErrorLog {
    private val log = ArrayList<PicaCompileError>();
    fun logError(msg: String,
                 loc: Location? = null,
                 astNode: AstNode? = null,
                 cause: Exception? = null) {
        val e = PicaCompileError(msg, loc, astNode, cause)
        System.err.println(e.toString())
        log.add(e)
    }

    fun logException(msg: String,
                     loc: Location? = null,
                     astNode: AstNode? = null,
                     cause: Exception? = null): PicaCompileException {
        logError(msg, loc, astNode, cause)
        return PicaCompileException(msg, loc, astNode, cause)
    }

    fun getErrors(): List<PicaCompileError> = log
}
