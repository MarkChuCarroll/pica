package org.goodmath.pica;

import org.goodmath.pica.ast.locations.Location;

public class PicaCompilationException extends Exception {
    private final Location loc;

    public PicaCompilationException(String msg, Location location) {
        super(msg);
        loc = location;
    }

    public Location getLocation() {
        return loc;
    }
}
