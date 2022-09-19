package org.goodmath.pica.types;


import org.goodmath.pica.ast.AstNode;
import org.goodmath.pica.ast.Definition;
import org.goodmath.pica.ast.FunctionDef;
import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.ast.bosons.BosonDef;
import org.goodmath.pica.ast.quarks.QuarkDef;
import org.goodmath.pica.errors.PicaCompilationException;


public class Defined {

    private Defined(String name, DefKind kind, AstNode definition) {
        this.name = name;
        this.kind = kind;
        this.definition = definition;

    }


    public static Defined importAlias(String name, Identifier referent) {
        return new Defined(name, DefKind.ImportAlias, referent);
    }

    public static Defined bosonDefinition(String name, BosonDef def) {
        return new Defined(name, DefKind.Boson, def);
    }

    public static Defined quarkDefinition(String name, QuarkDef def) {
        return new Defined(name, DefKind.Quark, def);
    }

    public static Defined functionDefinition(String name, FunctionDef func) {
        return new Defined(name, DefKind.Function, func);
    }

    public enum DefKind {
        Quark, Boson, Function, ImportAlias;
    }

    private final String name;
    public String getName() {
        return name;
    }

    private final AstNode definition;
    public Definition getDefinition() throws PicaCompilationException {
        if (kind != DefKind.ImportAlias) {
            return (Definition)definition;
        } else {
            throw new PicaCompilationException("Invalid use of import alias definition for " + name);
        }
    }

    public Identifier getAlias() throws PicaCompilationException {
        if (kind == DefKind.ImportAlias) {
            return (Identifier)definition;
        } else {
            throw new PicaCompilationException("Cannot use a non-alias as an alias definition " + name);
        }
    }

    private DefKind kind;

}
