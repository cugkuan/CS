package com.k.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class MyFirstPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        Logger.project = project
        def isApp = project.plugins.hasPlugin(AppPlugin)
        if (!isApp) {
            return
        }
        Logger.error("插件开始工作")
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new AutoInjectTransform(project))

        project.afterEvaluate {
            println("配置完成")

            AutoInjector.ignorePackages = ['android','com/google','org','androidx','kotlin',"Lorg"]
        }


    }
}