package org.goodmath.pica.cli;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.goodmath.pica.ast.Identifier;
import org.goodmath.pica.ast.PicaModule;
import org.goodmath.pica.compiler.PicaCompiler;
import org.goodmath.pica.errors.PicaCompilationException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "picac", version = "0", mixinStandardHelpOptions = true)
public class CompileCommand implements Runnable {

    @Option(names = { "-d", "--module-dir-path" },
        description = "A colon-separated list of directories to search for loadable modules")
    private List<File> dirs = List.of(new File((".")));

    @Parameters(paramLabel="ModuleName", description="A list of modules to compile",
        arity = "1..*")
    private List<String> modules;

    @Override
    public void run() {
        PicaCompiler c = new PicaCompiler(dirs);
        for (String moduleName: modules) {
            try {
               c.readModule(Identifier.parseIdentifier(moduleName));
            } catch (IOException e) {
                System.err.printf("IO error reading module %s\n", moduleName);
                System.exit(1);
            } catch (PicaCompilationException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
        for (PicaModule m: c.getParsedModules()) {
            System.out.println(">>>> Module " + m.getName());
            System.out.println(m.toString());
        }

    }


}
