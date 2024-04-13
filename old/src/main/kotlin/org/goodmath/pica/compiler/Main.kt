package org.goodmath.pica.compiler

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import org.goodmath.pica.RootScope
import java.io.File

object CompileCommand: CliktCommand(name="compile", help="Compile an Abyss program") {
    val modulesDirs by option("--modules-dirs",
        help="Colon-separated list of directories where modules should be loaded").default(".")

    val sources by argument(help="source files to compile").file().multiple()

    override fun run() {
        val sourceDirs = modulesDirs.split(":").map { File(it) }
        System.out.println("SourceDirs = $sourceDirs")
        val c = Compiler(sourceDirs, sources)
        try {
            c.parseModules()
        } catch (e: Exception) {
            System.err.println("Compile error: $e")
        }
        System.out.println(RootScope.twist().toString())
    }
}

fun main(args: Array<String>) {
    try {
        CompileCommand.main(args)
    } catch (e: Exception) {
        System.err.println("Error: ${e.message}")
        System.exit(1)
    }
}

