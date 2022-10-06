package org.goodmath.pica.errors;

public class PicaTypeError extends PicaCompilationError {
    public PicaTypeError(String expected, String actual) {
        this(expected, actual, null);
    }

    public PicaTypeError(String expected, String actual, Exception cause) {
        super(String.format("Expected a %s, but found %s", expected, actual), cause);
    }

    public PicaTypeError(String msg, Exception cause) {
        super(msg, cause);
    }

    public PicaTypeError(String msg) {
        this(msg, (Exception)null);
    }


}
