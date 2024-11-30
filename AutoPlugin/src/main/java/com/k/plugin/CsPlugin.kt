package com.k.plugin


import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class CsPlugin :Plugin<Project> {
    override fun apply(project: Project) {
        Logger.project = project
        Logger.info("CsPlugin:${project.name}")
        val extension =  project.extensions
        extension.findByType(ApplicationExtension::class.java)?:return
        CsPluginUtils.clear()
        val android = project.extensions.getByType(AndroidComponentsExtension::class.java)
        android.onVariants {  variant ->
           val csTask =  project.tasks.register("${variant.name}CsTask",CsTask::class.java)
            variant.artifacts
                .forScope(ScopedArtifacts.Scope.ALL)
                .use(csTask)
                .toTransform(
                    ScopedArtifact.CLASSES,
                    { it.allJars },
                    { it.allDirectories },
                    { it.output }
                )
        }
    }
}

