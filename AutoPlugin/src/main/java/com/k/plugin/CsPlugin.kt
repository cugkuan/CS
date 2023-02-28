package com.k.plugin


import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class CsPlugin :Plugin<Project> {
    override fun apply(project: Project) {
        Logger.project = project
        val isApp = project.plugins.hasPlugin(AppPlugin::class.java)
        if (!isApp) {
            return
        }
        project.extensions.create("csConfig",CsConfig::class.java)
        CsPluginUtils.clear()
        val android = project.extensions.getByType(AppExtension::class.java)
        android.registerTransform(CsTransform(project))
        project.afterEvaluate {
            CsPluginUtils.scanPackage = project.extensions.getByType(CsConfig::class.java).scanPackages
        }
    }
}

