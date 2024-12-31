package top.brightk.cs.process

import com.brightk.cs.core.annotation.CsUri
import com.brightk.cs.core.annotation.KspBridgeService
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.qizhidao.launchksp.processor.BaseProcessor
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
class KspProcessor(val environment: SymbolProcessorEnvironment) :
    BaseProcessor(environment) {

    private var isCsScan: Boolean = true
    private var csFinish = false
    val csServices = ArrayList<CsServiceNode>()

    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        log("CsKps:kspProcess开始运行")
        log(resolver.getModuleName().asString())
        if (isCsScan) {
            csServices.clear()
            val csServiceVisitor = CsServiceVisitor(this)
            val ksAnnotated = resolver.getSymbolsWithAnnotation(CsUri::class.java.name)
            ksAnnotated.forEach {
                it.accept(csServiceVisitor, Unit)
            }
            // 全部收集完成，开始生成中间文件
            log("CsService收集完成")
            if (csServices.isNotEmpty()) {
                CreateCsTransfer(codeGenerator, csServices)
                    .create()
            }
            isCsScan = false
            return ksAnnotated.toList()
        }
        // 生产中间产物
        val application = environment.options["application"]
        if (application == "true" && !csFinish) {
            log("主项目运行......")
            val csFinalServices = ArrayList<CsServiceNode>()
            resolver.getDeclarationsFromPackage(CS_TRANSFER_PACKET).forEach { declaration ->
                log("汇总：${declaration.simpleName.asString()}")
                declaration.annotations.forEach {
                    log("===>"+it.shortName.asString())
                    log("===>"+it.annotationType.toString())
                }
                declaration.getAnnotationsByType(KspBridgeService::class).forEach { node ->
                    log(node.toString())
                    val json = node.json
                    log(json)
                    csFinalServices.addAll(json.getServiceNodes())
                }
            }
            CreateFinalTransfer(codeGenerator, csFinalServices)
                .create()
            csFinish = true
        }
        return emptyList()
    }

    override fun finish() {
        super.finish()
    }
}