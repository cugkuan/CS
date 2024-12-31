package top.brightk.cs.process.create

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import top.brightk.cs.process.CS_TRANSFER_FINIAL
import top.brightk.cs.process.CS_TRANSFER_FINIAL_CLASS
import top.brightk.cs.process.CsInterceptorNode
import top.brightk.cs.process.CsServiceNode

class CreateFinalTransfer(
    private val codeGenerator: CodeGenerator,
    private val csService: List<CsServiceNode>,
    private val csInterceptors: List<CsInterceptorNode>
) : BaseTransfer() {

    fun create() {
        codeGenerator.createNewFile(
            Dependencies(false),
            CS_TRANSFER_FINIAL, CS_TRANSFER_FINIAL_CLASS, "java"
        )
            .use { stream ->
                with(stream) {
                    //import com.brightk.cs.CsPluginRegister
                    appendText("package ${CS_TRANSFER_FINIAL};")
                    newLine(2)
                    appendText("import com.brightk.cs.CsPluginRegister;")
                    newLine(1)
                    appendText("class $CS_TRANSFER_FINIAL_CLASS {")
                    newLine(1)
                    appendTextWithTab("public static void init(){")
                    newLine(1)
                    //cs
                    if (csService.isNotEmpty()) {
                        csService.forEach { service ->
                            appendTextWithTab(
                                "CsPluginRegister.registerService(\"${service.key}\",\"${service.className}\");",
                                2
                            )
                            newLine(1)
                        }
                        newLine(1)
                    }
                    if (csInterceptors.isNotEmpty()) {
                        csInterceptors.forEach { interceptor ->
                            appendTextWithTab(
                                "CsPluginRegister.registerInterceptor(\"${interceptor.className}\",${interceptor.priority},\"${interceptor.name}\");",
                                2
                            )
                            newLine(1)
                        }
                    }
                    newLine(1)
                    appendTextWithTab("}")
                    newLine(2)
                    appendText("}")

                }
            }
    }
}