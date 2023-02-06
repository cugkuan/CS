package com.k.plugin.transform

import com.android.build.api.transform.*
import com.k.plugin.CsUtils
import com.k.plugin.Logger
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.util.*
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
        if (isIncremental) {
            outputProvider.deleteAll()
        }
        inputs.forEach { input ->
            input.jarInputs.forEach { jarInput ->
                val task = Callable {
                    val status = jarInput.status
                    var destName = jarInput.file.name
                    val hexName = CsUtils.getMD5(jarInput.file.absolutePath)
                    val destFile: File = outputProvider.getContentLocation(
                        "$destName-$hexName",
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
                    val dest = outputProvider.getContentLocation(
                        directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY
                    )
                    scanDirectoryInput(directoryInput, dest)
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
        jarFile.entries().asIterator().forEach { entry ->
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

    /**
     * 这里如果使用增量更新很诡异，directoryInput.getChangedFiles 只包含了改变的文件
     */
    private fun scanDirectoryInput(directoryInput: DirectoryInput, destDir: File) {
        scanClass(directoryInput.file)
        FileUtils.copyDirectory(directoryInput.file, destDir)
    }

    private fun scanClass(file: File) {
        if (file.isFile) {
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