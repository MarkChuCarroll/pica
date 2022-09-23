package org.goodmath.pica.compiler;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;

import org.antlr.v4.runtime.dfa.DFA;

public class PicaErrorListener extends BaseErrorListener {
    private final String sourceFile;
    public PicaErrorListener(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    private int errorCount = 0;
    public int getErrorCount() {
        return errorCount;
    }


    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction,
            ATNConfigSet configs) {
        System.err.printf("INTERNAL CS error: %s; start=%d, stop=%d, pred=%d\n",
            dfa, startIndex, stopIndex, prediction);
    }

    @Override
    public void syntaxError(
            Recognizer<?, ?> recognizer,
            Object offendingSymbol,
            int line,
            int charPositionInLine,
            String msg,
            RecognitionException e) {
        errorCount++;
        System.err.printf("Parse error: %s (line %d, col %d): %s\n",
            sourceFile, line, charPositionInLine, msg);

    }


}
