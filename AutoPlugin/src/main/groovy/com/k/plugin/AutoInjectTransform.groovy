package com.k.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.k.plugin.AutoInjector
import org.gradle.api.Project
import org.gradle.internal.impldep.org.eclipse.jgit.annotations.NonNull

class AutoInjectTransform extends Transform {


    private Project project


    AutoInjectTransform(Project project) {
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


        Collection<TransformInput> inputs = transformInvocation.inputs
        TransformOutputProvider outputProvider = transformInvocation.outputProvider
        //删除之前的输出
        if (outputProvider != null) {
            outputProvider.deleteAll()
        }
        //查找服务组件
        inputs.each { TransformInput transformInput ->
            transformInput.directoryInputs.each { DirectoryInput directoryInput ->
                AutoInjector.findServiceClassInfo(directoryInput.file)
            }
            transformInput.jarInputs.each { JarInput jarInput ->
                AutoInjector.findServiceClassInfo(jarInput.file)
            }
        }
        Logger.error("transform 服务组件遍历完成")
        // 将服务进行自动注册
        inputs.each { TransformInput transformInput ->
            transformInput.directoryInputs.each { DirectoryInput directoryInput ->

                AutoInjector.registerTargetClassInfo(directoryInput.file)
                //下面的代码必须存在
                def dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
            transformInput.jarInputs.each { JarInput jarInput ->
                  AutoInjector.registerTargetClassInfo(jarInput.file)
                // 下面的代码必须存在
                def dest = transformInvocation.outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)
            }
        }
        Logger.error("transform 组件注册完成")
    }
}