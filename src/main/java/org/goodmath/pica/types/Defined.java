package org.goodmath.pica.types;


import lombok.Data;
import org.goodmath.pica.ast.Definition;

@Data
public class Defined {
    public enum DefKind {
        Quark, Boson, Function
    }

    private String name;
    private Definition ast;
    private DefKind kind;


}
