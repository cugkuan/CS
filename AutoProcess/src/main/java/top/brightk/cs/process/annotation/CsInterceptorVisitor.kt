package top.brightk.cs.process.annotation

import com.brightk.cs.core.annotation.Interceptor
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import top.brightk.cs.process.CsInterceptorNode
import top.brightk.cs.process.KspProcessor

class CsInterceptorVisitor(val process: KspProcessor, private val csInterceptors:MutableList<CsInterceptorNode>) : KSVisitorVoid() {

    @OptIn(KspExperimental::class)
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val name = classDeclaration.qualifiedName!!.asString()
        classDeclaration.getAnnotationsByType(Interceptor::class).forEach { node ->
            process.log("CsInterceptor:$name ==> ${node.name}  ${node.priority}")
            if (node.name.isEmpty()){
                throw IllegalArgumentException("CsInterceptor 没有设置 name")
            }
            csInterceptors.add(CsInterceptorNode(name, node.priority, node.name))
        }
    }
}