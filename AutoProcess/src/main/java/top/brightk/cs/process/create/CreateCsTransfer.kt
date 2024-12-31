package top.brightk.cs.process.create

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import top.brightk.cs.process.CS_TRANSFER_IMPORT_INTERCEPTOR
import top.brightk.cs.process.CS_TRANSFER_IMPORT_SERVICE
import top.brightk.cs.process.CS_TRANSFER_PACKET
import top.brightk.cs.process.CsInterceptorNode
import top.brightk.cs.process.CsServiceNode
import top.brightk.cs.process.toTransitContentJson


class CreateCsTransfer(
    private val codeGenerator: CodeGenerator,
    private val csService: List<CsServiceNode>,
    private val csInterceptorNodes: List<CsInterceptorNode>
) : BaseTransfer() {
    fun create() {
        val className = getCreateName()
        codeGenerator.createNewFile(Dependencies(false), CS_TRANSFER_PACKET, className, "kt")
            .use { stream ->
                with(stream) {
                    appendText("package $CS_TRANSFER_PACKET")
                    newLine(2)
                    appendText("import $CS_TRANSFER_IMPORT_SERVICE")
                    newLine(1)
                    appendText("import $CS_TRANSFER_IMPORT_INTERCEPTOR")
                    newLine(2)

                    // csService
                    if (csService.isNotEmpty()) {
                        val json = csService.toTransitContentJson()
                        appendText("@KspBridgeService(json = \"${json}\")")
                        newLine(1)
                        appendText("val csService:String? = null")
                        newLine(2)
                    }

                    if (csInterceptorNodes.isNotEmpty()) {
                        // csInterceptor
                        val interceptorJson = csInterceptorNodes.toTransitContentJson()
                        appendText("@KspBridgeInterceptor(json =  \"${interceptorJson}\")")
                        newLine(1)
                        appendText("val csInterceptor:String? = null")
                        newLine(2)
                    }
                }
            }


    }
}