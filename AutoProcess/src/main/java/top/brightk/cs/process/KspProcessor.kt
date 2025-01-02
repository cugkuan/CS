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
    private var csFinish = false
    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        log(resolver.getModuleName().asString())
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
                CreateCsTransfer(codeGenerator, csServices, csInterceptors)
                    .create()
            }
            isCsScan = false
            return ArrayList<KSAnnotated>().apply {
                addAll(ksAnnotated.toList())
                addAll(interceptorAnnotation.toList())
            }
        }
        val application = options["application"]
        if (application == "true" && !csFinish) {
            val csFinalServices = ArrayList<CsServiceNode>()
            val csFinalInterceptor = ArrayList<CsInterceptorNode>()
            resolver.getDeclarationsFromPackage(CS_TRANSFER_PACKET).forEach { declaration ->
                declaration.getAnnotationsByType(KspBridgeService::class).forEach { node ->
                    val json = node.json
                    csFinalServices.addAll(json.getServiceNodes())
                }
                declaration.getAnnotationsByType(KspBridgeInterceptor::class).forEach { node ->
                    val json = node.json
                    csFinalInterceptor.addAll(json.getInterceptors())
                }
            }
            log("一共有${csFinalServices.size}个CsService ${csFinalInterceptor.size}个Interceptor")
            CreateFinalTransfer(codeGenerator, csFinalServices, csFinalInterceptor)
                .create()
            csFinish = true
        }
        return emptyList()
    }

    override fun finish() {
        super.finish()
    }
}