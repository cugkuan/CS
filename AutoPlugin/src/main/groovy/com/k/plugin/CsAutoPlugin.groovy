package com.k.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class CsAutoPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        Logger.project = project
        def isApp = project.plugins.hasPlugin(AppPlugin)
        if (!isApp) {
            return
        }
        Logger.error("CS自动注册插件开始工作")
        project.extensions.create("csConfig",CsConfig.class)
        AutoInjector.clear()
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new CsTransform(project))

        project.afterEvaluate {
            AutoInjector.ignorePackages = ['android','com/google','org','androidx','kotlin',"Lorg","java"]
            AutoInjector.scanPackages =  project.csConfig.scanPackages
        }


    }
}