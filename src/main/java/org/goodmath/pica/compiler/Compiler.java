package org.goodmath.pica.compiler;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Optional;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.goodmath.pica.ast.AstBuilder;
import org.goodmath.pica.ast.Definition;
import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.parser.PicaGrammarLexer;
import org.goodmath.pica.parser.PicaGrammarParser;
import org.goodmath.pica.types.Defined;
import org.goodmath.pica.types.ModuleScope;
import org.goodmath.pica.types.Scope;

class PicaCompiler {

    private List<File> modulePath;

    public PicaCompiler(final List<File> modulePath) {
        this.modulePath = modulePath;
    }

    private ArrayDeque<Identifier> compileQueue = new ArrayDeque<Identifier>();

    public File identifierToFile(Identifier id) {
        for (File dir: modulePath) {
            File f = new File(id.toString().replace("::", "/") + ".schism");
            if (f.exists()) {
                return f;
            }
        }
        return null;
    }

    public void readModule(Identifier module) throws IOException {
        if (!Scope.RootScope.includesModule(module)) {
            if (module.getModule().isPresent()) {
                readModule(module.getModule().get());
            }
            var f = identifierToFile(module);
            readSourceFile(module, f);
        }
    }

    void readSourceFile(Identifier id, File sourceFile) throws IOException {
        if (!sourceFile.getName().endsWith(".pica")) {
            throw new RuntimeException("Source files must be named something.pica");
        }
        // create a CharStream that reads from standard input
        // create a CharStream that reads from standard input
        var input = new ANTLRInputStream(new FileReader(sourceFile));

        // create a lexer that feeds off of input CharStream
        var lexer = new PicaGrammarLexer(input);

        // create a buffer of tokens pulled from the lexer
        var tokens = new CommonTokenStream(lexer);


        // create a parser that feeds off the tokens buffer
        var parser = new PicaGrammarParser(tokens);

        ParseTree tree = parser.module(); // begin parsing at init rule
        var treeBuilder = new AstBuilder(sourceFile.getName());
        var walker = new ParseTreeWalker();
        walker.walk(treeBuilder, tree);
        var module = treeBuilder.getParsedModule();

        System.out.println(tree.toStringTree(parser)); // print LISP-style tree

        var scope = new ModuleScope(id, Optional.empty());
        for (Definition def:  module.getDefinitions()) {
            for (Defined defined: def.getDefinedNames()) {
                scope.setDefinition(defined.getName(), defined);
            }
        }
        Scope.RootScope.setModule(id, scope);
    }

}