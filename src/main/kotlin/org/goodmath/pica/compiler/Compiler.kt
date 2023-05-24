package org.goodmath.pica.compiler

import org.antlr.v4.runtime.CharStreams
import org.goodmath.pica.HadronScope
import org.goodmath.pica.RootScope
import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.SystemLocation
import org.goodmath.pica.parser.AstParser
import org.goodmath.pica.util.PicaErrorLog
import org.goodmath.pica.util.PicaIOException
import java.io.File

class Compiler(private val sourceDirs: List<File>, private val sources: List<File>) {

    fun parseModules() {
        for (source in sources) {
            modulesToCompile.addLast(Pair(source, sourcePathToModule(source)))
        }
        while (modulesToCompile.isNotEmpty()) {
            val (file, id) = modulesToCompile.removeFirst()
            if (RootScope.getHadronScope(id) == null) {
                parseModule(file, id)
            }
        }
    }

    private fun parseModule(file: File, id: Identifier) {
        println("Reading $file")
        val parser = AstParser(id)
        val loadedModule = parser.parse(CharStreams.fromFileName(file.canonicalPath))
        val scope = HadronScope(id, loadedModule)
        RootScope.setHadronScope(id, scope)
        for (imp in loadedModule.imports) {
            modulesToCompile.addLast(Pair(moduleToSourceFile(imp.hadronId), imp.hadronId))
        }
    }

    private val modulesToCompile = ArrayDeque<Pair<File, Identifier>>()

    fun moduleToSourceFile(moduleId: Identifier): File {
        val relativePath = File(moduleId.toString().replace("::", "/"))
        for (dir in sourceDirs) {
            val p = dir.resolve(relativePath)
            if (p.exists()) {
                return p
            }
        }
        throw PicaErrorLog.logException(
            PicaIOException("No source file found in source path for module $moduleId", null))
    }

    fun sourcePathToModule(file: File): Identifier {

        if (!file.path.endsWith(".pica")) {
            throw PicaErrorLog.logException(PicaIOException("Invalid source path $file", null))
        }
        for (dir in sourceDirs) {
            if (File(dir, file.path).exists()) {
                val relativePath = file.relativeTo(dir).path
                return Identifier.fromList(relativePath.dropLast(5).split("/"),
                    SystemLocation("source file $relativePath"))
            }
        }
        throw PicaErrorLog.logException(PicaIOException("Source file $file not found in source path", null))
    }

}
