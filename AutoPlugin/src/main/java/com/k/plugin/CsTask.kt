package com.k.plugin

import com.k.plugin.csinject.CSInjectClassVisitor
import com.k.plugin.csinterceptor.InterceptorClassVisitor
import com.k.plugin.csserch.ServiceClassVisitor
import com.k.plugin.transform.DefaultClassNameFilter
import com.k.plugin.transform.SearchServiceTransform
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Collections
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

abstract class CsTask : DefaultTask() {
    @get:InputFiles
    abstract val allJars: ListProperty<RegularFile>

    @get:InputFiles
    abstract val allDirectories: ListProperty<Directory>

    @get:OutputFile
    abstract val output: RegularFileProperty
    private val serviceClassInfos: MutableList<CsServiceClassInfo> =
        Collections.synchronizedList(ArrayList())
    private val interceptors: MutableList<InterceptorClassInfo> =
        Collections.synchronizedList(ArrayList())

    @TaskAction
    fun taskAction() {
        serviceClassInfos.clear()
        interceptors.clear()
        val begin = System.currentTimeMillis()
        Logger.info("Cs插件开始工作")
        scanAction()
        Logger.info("Cs开始进行代码注入")
        inject()
        val end = System.currentTimeMillis()
        Logger.error("Cs一共花费:${end - begin}毫秒")
    }

    private fun scanAction() {
        val begin = System.currentTimeMillis()
        val transform = SearchServiceTransform(allJars, allDirectories) { classBytes ->
            val classReader = ClassReader(classBytes)
            classReader.accept(ServiceClassVisitor { info ->
                println("Cs:Service ${info.className} => ${info.url}")
                serviceClassInfos.add(info)
            }, 0)
            classReader.accept(InterceptorClassVisitor { info ->
                interceptors.add(info)
            }, 0)
        }
        transform.startTransform()
        Logger.error("Cs:一共找到${serviceClassInfos.size}个服务,${interceptors.size}个Interceptor;共花费${System.currentTimeMillis() - begin}毫秒")
    }

    private fun inject() {
        val outputFile = output.get().asFile
        println("LaunchInjectTask outputFile:${outputFile.absolutePath}")
        val allJarList = allJars.get()
        var tmpClassesFile: File? = null
        if (allJarList.size == 1 && allJarList[0].asFile.absolutePath == outputFile.absolutePath) {
            val tmpFile = File(outputFile.parentFile.absolutePath, "tmpFile.jar")
            if (tmpFile.exists()) {
                tmpFile.delete()
            }
            tmpClassesFile = tmpFile
            allJarList[0].asFile.renameTo(tmpFile)
        }

        JarOutputStream(
            BufferedOutputStream(FileOutputStream(outputFile))
        ).use { outputStream ->
            tmpClassesFile?.let { tmpFile ->
                jarFileHandle(tmpFile, outputStream)
            } ?: run {
                allJars.get().forEach { regularFile ->
                    val file = regularFile.asFile
                    jarFileHandle(file, outputStream)
                }
            }
            allDirectories.get().forEach { directory ->
                val directoryFile = directory.asFile
                directoryFile.walk().forEach dirsForEach@{ file ->
                    if (file.isDirectory) return@dirsForEach
                    val relativePath = directoryFile.toURI().relativize(file.toURI()).path
                    val relativeFilePath = relativePath.replace(File.separatorChar, '/')
                    handleClassFile(
                        outputStream = outputStream,
                        fileInputStream = file.inputStream(),
                        filePath = relativeFilePath
                    )
                }

            }
        }
    }

    private fun jarFileHandle(file: File, outputStream: JarOutputStream) {
        JarFile(file).use { jarFile ->
            jarFile.entries().iterator().forEach jarIterator@{ jarEntry ->
                val jarEntryName = jarEntry.name
                if (jarEntry.isDirectory || DefaultClassNameFilter.isSkipTransformJarFile(
                        jarEntryName
                    )
                ) {
                    return@jarIterator
                }
                handleClassFile(
                    outputStream = outputStream,
                    fileInputStream = jarFile.getInputStream(jarEntry),
                    filePath = jarEntryName
                )
            }
        }
    }

    private fun handleClassFile(
        outputStream: JarOutputStream,
        fileInputStream: InputStream,
        filePath: String
    ) {
        outputStream.putNextEntry(JarEntry(filePath))
        val isInjectFile = DefaultClassNameFilter.isTargetClass(filePath)
        fileInputStream.use { inputStream ->
            inputStream.takeIf { isInjectFile }
                ?.readAllBytes()
                ?.takeIf { it.isNotEmpty() }
                ?.let { byteArr ->
                    val fileByteArr = generateNewClassFileByteArr(byteArr)
                    outputStream.write(fileByteArr)
                } ?: inputStream.copyTo(outputStream)
        }
        outputStream.closeEntry()
    }

    private fun generateNewClassFileByteArr(byteArray: ByteArray): ByteArray {
        val classReader = ClassReader(byteArray)
        val classWriter =
            ClassWriter(
                classReader,
                ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS
            )
        val targetClassVisitor =
            CSInjectClassVisitor(
                classWriter,
                services = serviceClassInfos,
                interceptors = interceptors
            )
        classReader.accept(
            targetClassVisitor,
            ClassReader.EXPAND_FRAMES
        )
        return classWriter.toByteArray()
    }


}