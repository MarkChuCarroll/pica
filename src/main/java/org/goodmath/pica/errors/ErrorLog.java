package org.goodmath.pica.errors;

import java.util.ArrayList;
import java.util.List;

public class ErrorLog {
    private ErrorLog() {
    }

    public static void logError(PicaCompilationError e) {
        errors.add(e);
    }

    public static List<PicaCompilationError> getErrors() {
        return errors;
    }
    private static List<PicaCompilationError> errors = new ArrayList<>();
}
