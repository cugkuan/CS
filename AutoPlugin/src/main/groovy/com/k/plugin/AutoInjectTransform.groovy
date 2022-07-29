package com.k.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.k.plugin.AutoInjector
import org.gradle.api.Project
import org.gradle.internal.impldep.org.eclipse.jgit.annotations.NonNull

class AutoInjectTransform extends  Transform{


    private Project project


    AutoInjectTransform(Project project){
        this.project = project
    }



    @Override
    String getName() {
        return "autoInject"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }


    @Override
    void transform(@NonNull TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        Logger.error("transform开始工作----${project.name}")
        transformInvocation.getInputs().each {TransformInput transformInput ->
            transformInput.directoryInputs.each { DirectoryInput directoryInput ->
                AutoInjector.findServiceClassInfo(directoryInput.file)
            }
            transformInput.getJarInputs().each { JarInput jarInput ->
                AutoInjector.findServiceClassInfo(jarInput.file)
            }
        }

        transformInvocation.getInputs().each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->

                AutoInjector.findTargetClassInfo(directoryInput,transformInvocation.outputProvider)
            }
//            if (AutoInjector.targetClassInfo != null){
//                return
//            }
//            input.jarInputs.each { JarInput jarInput ->
//                def dest = transformInvocation.outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
//
//                AutoInjector.findTargetClassInfo(dest)
//
//                FileUtils.copyFile(jarInput.file, dest)
//            }
        }

        AutoInjector.verify()

    }
}