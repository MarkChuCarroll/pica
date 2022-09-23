package org.goodmath.pica.compiler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.goodmath.pica.ast.locations.SourceFileLocation;
import org.goodmath.pica.errors.PicaCompilationException;
import org.goodmath.pica.ast.AstBuilder;
import org.goodmath.pica.ast.Definition;
import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.ast.PicaModule;
import org.goodmath.pica.ast.UseDef;
import org.goodmath.pica.errors.PicaSyntaxException;
import org.goodmath.pica.parser.PicaGrammarLexer;
import org.goodmath.pica.parser.PicaGrammarParser;
import org.goodmath.pica.types.Defined;
import org.goodmath.pica.types.ModuleScope;
import org.goodmath.pica.types.RootScope;
import org.goodmath.pica.types.Scope;

public class PicaCompiler {

    private final List<File> modulePath;

    public PicaCompiler(final List<File> modulePath) {
        this.modulePath = modulePath;
    }

    public List<PicaModule> getParsedModules() {
        return RootScope.root.getModules();
    }

    private final ArrayDeque<Identifier> compileQueue = new ArrayDeque<Identifier>();

    public File identifierToFile(Identifier id) {
        for (File dir: modulePath) {
            File f = new File(dir, id.toString().replace("::", "/") + ".pica");
            if (f.exists()) {
                return f;
            }
        }
        return null;
    }

    public void readModule(Identifier module) throws IOException, PicaCompilationException {
        if (!RootScope.root.includesModule(module)) {
            if (module.getModule().isPresent()) {
                readModule(module.getModule().get());
            }
            var f = identifierToFile(module);
            readSourceFile(module, f);
        }
    }

    void readSourceFile(Identifier id, File sourceFile) throws PicaCompilationException {
        if (!sourceFile.getName().endsWith(".pica")) {
            throw new PicaCompilationException("Source files must be named module.pica, but found '"
                + sourceFile.getName() + "'");
        }

        CharStream input;
        try {
            input = CharStreams.fromFileName(sourceFile.getPath());
        } catch (IOException e) {
            throw new PicaCompilationException(String.format("File %s not found", sourceFile));
        }

        // create a lexer that feeds off of input CharStream
        var lexer = new PicaGrammarLexer(input);

        // create a buffer of tokens pulled from the lexer
        var tokens = new CommonTokenStream(lexer);


        // create a parser that feeds off the tokens buffer
        var parser = new PicaGrammarParser(tokens);
        parser.removeErrorListeners();
        var errorListener = new PicaErrorListener(sourceFile.getPath());
        parser.addErrorListener(errorListener);
        var treeBuilder = new AstBuilder(sourceFile.getName());

        try {
            // Parse a module and generate an AST.
            ParseTree tree = parser.module();
            var walker = new ParseTreeWalker();
            walker.walk(treeBuilder, tree);
            var module = treeBuilder.getParsedModule();

            if (errorListener.getErrorCount() > 0) {
                System.err.printf("Aborting compilation due to %d errors parsing %s\n",
                    errorListener.getErrorCount(), sourceFile.getPath());
            }
            // Build a scope of named definitions from the AST.
            var scope = new ModuleScope(id, Optional.empty(), module);
            for (UseDef u: module.getUses()) {
                compileQueue.push(u.getId());
                for (Defined d: u.getDefinedNames()) {
                    scope.setDefinition(d.getName(), d);
                }
            }
            for (Definition def : module.getDefinitions()) {
                for (Defined defined : def.getDefinedNames()) {
                    scope.setDefinition(defined.getName(), defined);
                }
            }
            RootScope.root.setModule(id, scope);
        } catch (RecognitionException re) {
            var loc = new SourceFileLocation(sourceFile.toString(), re.getOffendingToken().getLine(),
                    re.getOffendingToken().getCharPositionInLine());
            throw new PicaSyntaxException(re.toString(), loc);
        }
    }

    public void typeCheckDefinition(Identifier id) throws PicaCompilationException {
        Defined def = RootScope.root.getDefinition(id).orElseThrow(() ->
                new PicaCompilationException("Definition of " + id + " not found"));
        if (def.getKind() == Defined.DefKind.Quark) {
            typeCheckQuarkDefinition(def.getDefinition(), Collections.emptyList());
        }
    }

    /**
     * Typecheck a definition.
     * @param definition the definition to check
     * @param typeArgs for the typeargs, if known. If this list is empty, then
     *                new typevars will be generated for the definition for checking.
     * @throws PicaCompilationException
     */
    private void typeCheckQuarkDefinition(Definition definition, List<TypeVar> typeArgs) throws PicaCompilationException {

    }


}