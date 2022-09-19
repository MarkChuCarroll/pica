package org.goodmath.pica.errors;

import org.goodmath.pica.ast.locations.Location;

public class PicaSyntaxException extends PicaCompilationException {
    public PicaSyntaxException(String msg, Location loc) {
        super(String.format("Syntax error at %s: %s", msg, loc));
    }
}
