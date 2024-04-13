package org.goodmath.pica.util

import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Location
import java.io.IOException

class PicaCompileError(val msg: String,
                       val loc: Location? = null,
                       val astNode: AstNode? = null,
                       val cause: Throwable? = null) {
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

open class PicaCompileException(val msg: String,
                                val loc: Location? = null,
                                val astNode: AstNode? = null,
                                cause: Throwable? = null): Exception(msg, cause) {
}

class PicaIOException(msg: String, cause: IOException?): PicaCompileException(msg, null, null, cause)
class PicaTypeException(msg: String, loc: Location, ast: AstNode): PicaCompileException(msg, loc, ast, null)

class PicaSyntaxException(msg: String, loc: Location, ast: AstNode): PicaCompileException(msg, loc, ast, null)

class PicaInternalException(msg: String): PicaCompileException(msg, null, null, null)

object PicaErrorLog {
    private val log = ArrayList<PicaCompileError>();


    fun logError(msg: String,
                 loc: Location? = null,
                 astNode: AstNode? = null,
                 cause: Throwable? = null) {
        val e = PicaCompileError(msg, loc, astNode, cause)
        System.err.println(e.toString())
        log.add(e)
    }

    fun logException(e: PicaCompileException): PicaCompileException {
        logError(e.msg, e.loc, e.astNode, e.cause)
        return e
    }

    fun getErrors(): List<PicaCompileError> = log
}
