package com.k.plugin


import com.android.build.api.instrumentation.*
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.AppPlugin
import com.k.plugin.csinject.CsClassVisitorFactory
import com.k.plugin.csserch.CsSearchClassVisitorFactory
import org.gradle.api.Plugin
import org.gradle.api.Project
abstract class CsPlugin :Plugin<Project> {
    override fun apply(project: Project) {
        Logger.project = project
        val isApp = project.plugins.hasPlugin(AppPlugin::class.java)
        if (!isApp) {
            return
        }
        Logger.error("CS自动注册插件开始工作")
        project.extensions.create("csConfig",CsConfig::class.java)
        CsPluginUtils.clear()
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        // 寻找服务
        androidComponents.onVariants { variant ->
            variant.instrumentation.transformClassesWith(CsSearchClassVisitorFactory::class.java,InstrumentationScope.ALL){
            }
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
        }
        // 注入代码
        androidComponents.onVariants { variant ->
            variant.instrumentation.transformClassesWith(
                CsClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) {
            }
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
        }
    }
}

