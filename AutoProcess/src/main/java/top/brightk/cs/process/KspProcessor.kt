package top.brightk.cs.process

import com.brightk.cs.core.annotation.CsUri
import com.brightk.cs.core.annotation.Interceptor
import com.brightk.cs.core.annotation.KspBridgeInterceptor
import com.brightk.cs.core.annotation.KspBridgeService
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import top.brightk.cs.process.annotation.CsInterceptorVisitor
import top.brightk.cs.process.annotation.CsServiceVisitor
import top.brightk.cs.process.create.CreateCsTransfer
import top.brightk.cs.process.create.CreateFinalTransfer

const val CS_TRANSFER_FINIAL = "com.brightk.cs"
const val CS_TRANSFER_FINIAL_CLASS = "CsInit"
const val CS_TRANSFER_PACKET = "com.brightk.cs.transfer"
const val CS_TRANSFER_IMPORT_SERVICE = "com.brightk.cs.core.annotation.KspBridgeService"
const val CS_TRANSFER_IMPORT_INTERCEPTOR = "com.brightk.cs.core.annotation.KspBridgeInterceptor"

/**
 * 针对非增量更新设计
 */
class KspProcessor(environment: SymbolProcessorEnvironment) :
    BaseProcessor(environment) {
    private var isCsScan: Boolean = true
    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        log(resolver.getModuleName().asString())
        val internalModuleName = resolver.getModuleName().asString()
            .replace("-", "_")
            .replace(".", "_")

        if (isCsScan) {
            val csServices = ArrayList<CsServiceNode>()
            val csInterceptors = ArrayList<CsInterceptorNode>()
            val csServiceVisitor = CsServiceVisitor(this, csServices)
            val ksAnnotated = resolver.getSymbolsWithAnnotation(CsUri::class.java.name)
            ksAnnotated.forEach {
                it.accept(csServiceVisitor, Unit)
            }
            val csInterceptorVisitor = CsInterceptorVisitor(this, csInterceptors)
            val interceptorAnnotation =
                resolver.getSymbolsWithAnnotation(Interceptor::class.java.name)
            interceptorAnnotation.forEach {
                it.accept(csInterceptorVisitor, Unit)
            }
            if (csServices.isNotEmpty() || csInterceptors.isNotEmpty()) {
                CreateCsTransfer(
                    internalModuleName = internalModuleName,
                    codeGenerator,
                    csServices,
                    csInterceptors
                )
                    .create()
            }
            isCsScan = false
            return ArrayList<KSAnnotated>().apply {
                addAll(ksAnnotated.toList())
                addAll(interceptorAnnotation.toList())
            }
        }
        return  emptyList()
    }

    override fun finish() {
        super.finish()
    }
}