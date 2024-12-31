package top.brightk.cs.process.create

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import top.brightk.cs.process.CS_TRANSFER_FINIAL
import top.brightk.cs.process.CS_TRANSFER_FINIAL_CLASS
import top.brightk.cs.process.CsServiceNode

class CreateFinalTransfer(val codeGenerator: CodeGenerator,
                          val csService:List<CsServiceNode>) : BaseTransfer() {

    fun create(){
        codeGenerator.createNewFile(Dependencies(false),
            CS_TRANSFER_FINIAL,CS_TRANSFER_FINIAL_CLASS,"java")
            .use { stream ->
                with(stream){
                    //import com.brightk.cs.CsPluginRegister
                    appendText("package ${CS_TRANSFER_FINIAL};")
                    newLine(2)
                    appendText("import com.brightk.cs.CsPluginRegister;")
                    newLine(1)
                    appendText("class $CS_TRANSFER_FINIAL_CLASS {")
                    newLine(1)
                    appendTextWithTab("public static void init(){")
                    newLine(1)
                    csService.forEach { service ->
                        appendTextWithTab("CsPluginRegister.registerService(\"${service.key}\",\"${service.className}\");",2)
                        newLine(1)
                    }
                    newLine(1)
                    appendTextWithTab("}")
                    newLine(2)
                    appendText("}")

                }
            }
    }
}