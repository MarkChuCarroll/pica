package org.goodmath.pica.parser

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.PicaErrorLog

class PicaErrorListener(val moduleId: Identifier) : BaseErrorListener() {

    override fun syntaxError(
        recognizer: Recognizer<*, *>,
        offendingSymbol: Any,
        line: Int,
        charPositionInLine: Int,
        msg: String,
        e: RecognitionException?
    ) {
        PicaErrorLog.logError(msg, loc = Location(moduleId.toString(), line, charPositionInLine),
            cause = e)
    }

}