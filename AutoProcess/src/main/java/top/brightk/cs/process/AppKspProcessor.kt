package top.brightk.cs.process

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import top.brightk.cs.process.create.CreateFinalTransfer

private const val SERVICE_QUALIFIER = "com.brightk.cs.core.annotation.KspBridgeService"
private const val INTERCEPTOR_QUALIFIER = "com.brightk.cs.core.annotation.KspBridgeInterceptor"

class AppKspProcessor(environment: SymbolProcessorEnvironment) :
    BaseProcessor(environment) {

    private var csFinish = false
    val csFinalServices = ArrayList<CsServiceNode>()
    val csFinalInterceptor = ArrayList<CsInterceptorNode>()


    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val application = options["application"]
        if (application == "true") {
            // New implementation starts here
            resolver.getDeclarationsFromPackage(CS_TRANSFER_PACKET).forEach { declaration ->
                declaration.annotations.forEach { annotation ->
                    // 1. 解析注解的全限定名
                    val qualifiedName =
                        annotation.annotationType.resolve().declaration.qualifiedName?.asString()
                    // 2. 提取名为 "json" 的参数值
                    val jsonValue = annotation.arguments
                        .find { it.name?.asString() == "json" }
                        ?.value as? String
                    if (!jsonValue.isNullOrBlank()) {
                        log("Cs work: processing json for annotation -> $qualifiedName")
                        try {
                            // 3. 根据注解名，分发解析逻辑
                            when (qualifiedName) {
                                SERVICE_QUALIFIER -> csFinalServices.addAll(jsonValue.getServiceNodes())
                                INTERCEPTOR_QUALIFIER -> csFinalInterceptor.addAll(jsonValue.getInterceptors())
                            }
                        } catch (e: Exception) {
                            error("Cs-->Failed to parse json: $jsonValue. Error: ${e.message}")
                        }
                    }
                }
                log("一共有${csFinalServices.size}个CsService ${csFinalInterceptor.size}个Interceptor")
            }
            return emptyList()
        }
        return  emptyList()
    }

    override fun finish() {
        val application = options["application"]
        if (application == "true" && !csFinish) {
            val stableServices = csFinalServices
                .distinctBy { it.key }
            val stableInterceptor = csFinalInterceptor
                .distinctBy { it.className + it.name + it.priority }
            CreateFinalTransfer(codeGenerator, stableServices, stableInterceptor)
                .create()
            csFinish = true
        }
    }
}