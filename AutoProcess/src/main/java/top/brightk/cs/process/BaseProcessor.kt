package top.brightk.cs.process

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import java.io.OutputStream

abstract class BaseProcessor(private val env: SymbolProcessorEnvironment) : SymbolProcessor {
    protected val codeGenerator: CodeGenerator = env.codeGenerator
    private val logger: KSPLogger = env.logger
    protected val options: Map<String, String> = env.options

    protected fun OutputStream.appendText(str: String) {
        write(str.toByteArray())
    }

    protected fun OutputStream.newLine(count: Int = 1) {
        repeat(count) {
            appendText("\n")
        }
    }

    protected fun OutputStream.appendTextWithTab(text: String, count: Int = 1) {
        repeat(count) {
            appendText("\t")
        }
        appendText(text)
    }

    fun log(text: String) {
        logger.warn("CsKsp --> $text")
    }
}