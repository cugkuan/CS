package top.brightk.cs.process.annotation

import com.brightk.cs.core.ServiceType
import com.brightk.cs.core.annotation.CsUri
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import top.brightk.cs.process.CsServiceNode
import top.brightk.cs.process.CsUtils
import top.brightk.cs.process.KspProcessor
import kotlin.math.log

class CsServiceVisitor(val process: KspProcessor) : KSVisitorVoid() {

    @OptIn(KspExperimental::class)
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

        val name = classDeclaration.qualifiedName!!.asString()
        classDeclaration.getAnnotationsByType(CsUri::class).forEach { csUrl ->
            val url = csUrl.uri
            val type = csUrl.type
            val flag = when (type) {
                ServiceType.SINGLE -> {
                    'b'
                }
                ServiceType.NEW -> {
                    'c'
                }
                else -> {
                    'a'
                }
            }
            process.log("CsService:$name => $url  $type")
            process.csServices.add(CsServiceNode(name+flag,CsUtils.getKey(url)))
        }

    }
}