package com.k.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.k.plugin.csinject.CSInjectClassVisitor
import com.k.plugin.csinterceptor.InterceptorClassVisitor
import com.k.plugin.csserch.ServiceClassVisitor
import com.k.plugin.transform.SearchServiceTransform
import org.apache.commons.io.FileUtils
import org.apache.tools.ant.taskdefs.Java
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry
import kotlin.collections.ArrayList

class CsTransform(val project: Project) : Transform() {


    override fun getName() = "CsTransform"
    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return true
    }

    override fun transform(transformInvocation: TransformInvocation) {

        val begin = System.currentTimeMillis()
        Logger.error("CS自动注册插件开始工作.........")
        val serviceClassInfos: MutableList<CsServiceClassInfo> =
            Collections.synchronizedList(ArrayList())
        val interceptors: MutableList<InterceptorClassInfo> =
            Collections.synchronizedList(ArrayList())
        val transform = SearchServiceTransform(transformInvocation) { classBytes ->
            val classReader = ClassReader(classBytes)
            classReader.accept(ServiceClassVisitor { info ->
                serviceClassInfos.add(info)
            }, 0)
            classReader.accept(InterceptorClassVisitor { info ->
                interceptors.add(info)
            }, 0)
        }
        transform.startTransform()
        Logger.error("一共找到${serviceClassInfos.size}个服务,${interceptors.size}个Interceptor;共花费${System.currentTimeMillis() - begin}毫秒")
        serviceClassInfos.forEach {
            Logger.info("${it.className}==>${it.url}")
        }
        val targetFile = transform.getInjectFile()
        if (targetFile != null) {
            try {
                val jarFile = JarFile(targetFile.first)
                val tempJar = File(targetFile.second.absolutePath.replace(".jar", "temp.jar"))
                if (tempJar.exists()) {
                    FileUtils.forceDelete(tempJar)
                }
                val jarOutputStream = JarOutputStream(FileOutputStream(tempJar))
                jarOutputStream.use {  jarOutputStream ->
                    jarFile.entries().iterator().forEach { entry ->
                        val zipEntry = ZipEntry(entry.name)
                        jarOutputStream.putNextEntry(zipEntry)
                        if (entry.name == "com/brightk/cs/CS.class") {
                            jarFile.getInputStream(entry)?.use { stream ->
                                stream.readAllBytes()?.takeIf {
                                    it.isNotEmpty()
                                }?.let {
                                    val classReader = ClassReader(it)
                                    val classWriter =
                                        ClassWriter(
                                            classReader,
                                            ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS
                                        )
                                    val targetClassVisitor =
                                        CSInjectClassVisitor(classWriter, serviceClassInfos,interceptors)
                                    classReader.accept(targetClassVisitor, ClassReader.EXPAND_FRAMES)
                                    jarOutputStream.write(classWriter.toByteArray())
                                }
                            } ?: run {
                                jarOutputStream.write(jarFile.getInputStream(entry).readAllBytes())
                            }
                        } else {
                            jarOutputStream.write(jarFile.getInputStream(entry).readAllBytes())
                        }
                    }
                }
                // 直接 rename windows 有问题，mac 上没问题
                FileUtils.copyFile(tempJar,targetFile.second)
                FileUtils.forceDelete(tempJar)
            } catch (e: Exception) {
                e.printStackTrace()
                Logger.error(e.message)
            }
        } else {
            Logger.error("未找到注入目标")
        }
        Logger.error("CS工作结束!一共花费${System.currentTimeMillis() - begin}毫秒")
    }
}