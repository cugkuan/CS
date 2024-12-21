package top.brightk.cs.process

import com.brightk.cs.core.annotation.CsUri
import com.brightk.cs.core.annotation.KspBridgeService
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.qizhidao.launchksp.processor.BaseProcessor

internal class KspProcessor(val environment: SymbolProcessorEnvironment) :
    BaseProcessor(environment) {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        log("CsKps:kspProcess开始运行")
        log(CsUri::class.java.name)
        log(KspBridgeService::class.java.name)
        return emptyList()
    }
    override fun finish() {
        super.finish()
    }

}