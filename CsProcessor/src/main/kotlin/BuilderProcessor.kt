import com.brightk.cs.core.ServiceType
import com.brightk.cs.core.annotation.CsUri
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import java.io.File
import java.io.OutputStream
import java.util.Collections
import java.util.logging.Logger
import kotlin.math.log


class BuilderProcessor(
    val codeGenerator: CodeGenerator,
    val logger: KSPLogger
) : SymbolProcessor {
    private var isProcess = false
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (isProcess) return emptyList()
        val csServiceClassInfos = Collections.synchronizedList(ArrayList<CsServiceClassInfo>())
        val symbols = resolver.getSymbolsWithAnnotation(CsUri::class.java.name)
        val ret = symbols.filter { !it.validate() }.toList()
        symbols
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach { symbol ->
                val ksAnnotation = symbol.annotations.getAnnotation(CsUri::class.java.simpleName)
                val url = ksAnnotation.arguments.getParameterValue<String>("uri")
                val flag = when (ksAnnotation.arguments.getParameterValue<Any>("type").toString()) {
                    "com.brightk.cs.core.ServiceType.SINGLE" -> "b"
                    "com.brightk.cs.core.ServiceType.NEW" -> "c"
                    else -> "a"
                }
                val className = (symbol as KSClassDeclaration).qualifiedName!!.asString()
                val serviceClassInfo = CsServiceClassInfo(className + flag, url)
                csServiceClassInfos.add(serviceClassInfo)
            }
        // 创建 Java文件
        val packageName = "com.brightk.cs.processor"
        val className = "CS\$\$Processor"
        val file = codeGenerator.createNewFile(Dependencies(false), packageName, className, "java")

        file.appendText("package $packageName;\n\n")
        file.appendText("import com.brightk.cs.CsPluginRegister;\n\n")
        file.appendText("import com.brightk.cs.CsProcessor;\n\n")
        file.appendText("public class $className implements CsProcessor {\n\n")
        // 构建方法
        file.appendText("@Override\n")
        file.appendText("public void process(){\n")
        csServiceClassInfos.forEach { info ->
            logger.warn("${info.className} --> ${info.url}")
            file.appendText("CsPluginRegister.register(\"${info.urlKey}\",\"${info.className}\");\n")
        }
        // 填充内容
        file.appendText("}\n\n")
        file.appendText("}\n")
        file.close()
        isProcess = true
        return ret
    }

    override fun onError() {
        super.onError()
    }

    override fun finish() {
        super.finish()
    }
}

class BuilderProcessorProvider : SymbolProcessorProvider {
    override fun create(
        env: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return BuilderProcessor(env.codeGenerator, env.logger)
    }
}