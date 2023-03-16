package com.k.plugin.transform

import com.android.build.api.transform.*
import com.k.plugin.Logger
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicReference
import java.util.jar.JarFile

/**
 * 这一步处理了增量更新了
 */
class SearchServiceTransform(
    transformInvocation: TransformInvocation,
    private val searchCallBack: (classBytes: ByteArray) -> Unit,
) {
    private val inputs: Collection<TransformInput> = transformInvocation.inputs
    private val outputProvider: TransformOutputProvider = transformInvocation.outputProvider
    private val isIncremental = transformInvocation.isIncremental
    private val filter: ClassNameFilter = DefaultClassNameFilter()
    private val executor: ExecutorService = ForkJoinPool.commonPool()
    private val tasks: MutableList<Callable<Int>> = ArrayList()

    private var targetFile: AtomicReference<Pair<File, File>> =
        AtomicReference<Pair<File, File>>(null)


    fun getInjectFile(): Pair<File, File>? = targetFile.get()
    fun startTransform() {
        Logger.error("本次编译为：${if (isIncremental) "增量" else "非增量"}编译")
        if (!isIncremental){
            outputProvider.deleteAll()
        }
        inputs.forEach { input ->
            input.jarInputs.forEach { jarInput ->
                val task = Callable {
                    var destName = jarInput.file.name.let {
                        if (it.endsWith(".jar")) {
                            it.substring(0, it.length - 4)
                        } else {
                            it
                        }
                    }
                    val hexName = DigestUtils.md5Hex(jarInput.file.absolutePath)
                    val status = jarInput.status
                    val destFile: File = outputProvider.getContentLocation(
                        "${hexName}_$destName",
                        jarInput.contentTypes, jarInput.scopes, Format.JAR
                    )
                    val jarFile = JarFile(jarInput.file)
                    if (isIncremental) {
                        when (status) {
                            Status.NOTCHANGED -> {
                                scanJar(jarFile, jarInput.file, destFile)
                            }
                            Status.ADDED, Status.CHANGED -> {
                                scanJar(jarFile, jarInput.file, destFile)
                                FileUtils.copyFile(jarInput.file, destFile)
                            }
                            Status.REMOVED -> {
                                if (destFile.exists()) {
                                    FileUtils.forceDelete(destFile)
                                }
                            }
                        }
                    } else {
                        scanJar(jarFile, jarInput.file, destFile)
                        FileUtils.copyFile(jarInput.file, destFile)
                    }
                    0
                }
                tasks.add(task)
            }
            input.directoryInputs.forEach { directoryInput ->
                val task = {
                    scanClass(directoryInput.file)
                    val dest = outputProvider.getContentLocation(
                        directoryInput.name, directoryInput.contentTypes,
                        directoryInput.scopes, Format.DIRECTORY
                    )
                    FileUtils.copyDirectory(directoryInput.file, dest)
                    0
                }
                tasks.add(task)
            }
        }
        executor.invokeAll(tasks)

    }

    private fun process(classBytes: ByteArray) {
        searchCallBack.invoke(classBytes)
    }

    private fun scanJar(jarFile: JarFile, inputFile: File, destFile: File): Boolean {
        var isIncludeTarget = false
        jarFile.entries().iterator().forEach { entry ->
            try {
                if (filter.filter(entry.name).not()) {
                    jarFile.getInputStream(entry)?.use { stream ->
                        stream.readAllBytes()?.takeIf {
                            it.isNotEmpty()
                        }?.let {
                            process(it)
                        }
                    }
                }
                if (filter.isTargetClass(entry.name)) {
                    targetFile.set(Pair(inputFile, destFile))
                    isIncludeTarget = true
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        jarFile.close()
        return isIncludeTarget
    }

    private fun scanClass(file: File) {
        if (file.isFile) {
            Logger.error(file.name)
            if (filter.filter(file.name).not()) {
                file.inputStream().use { stream ->
                    stream.readAllBytes().takeIf {
                        it.isNotEmpty()
                    }?.let {
                        process(it)
                    }
                }
            }
        } else {
            file.listFiles().forEach {
                scanClass(it)
            }
        }
    }
}