package top.brightk.cs.process.create

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import top.brightk.cs.process.CS_TRANSFER_IMPORT_SERVICE
import top.brightk.cs.process.CS_TRANSFER_PACKET
import top.brightk.cs.process.CsServiceNode
import top.brightk.cs.process.toTransitContentJson


class CreateCsTransfer(val codeGenerator: CodeGenerator,
                       val csService:List<CsServiceNode>) : BaseTransfer() {
   fun create(){
       val className = getCreateName()
       codeGenerator.createNewFile(Dependencies(false), CS_TRANSFER_PACKET,className,"kt")
           .use { stream ->
               with(stream){
                   appendText("package $CS_TRANSFER_PACKET")
                   newLine(2)
                   appendText("import $CS_TRANSFER_IMPORT_SERVICE")
                   newLine(2)
                   val json = csService.toTransitContentJson()
                   appendText("@KspBridgeService(json = \"${json}\")")
                   newLine(1)
                   appendText("val csService:String? = null")
                   newLine(2)
               }
           }


    }
}