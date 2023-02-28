package com.k.plugin.transform

import com.android.build.api.transform.*
import com.k.plugin.CsUtils
import com.k.plugin.Logger
import org.apache.commons.io.FileUtils
import org.gradle.internal.impldep.com.google.common.io.Files
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
        if (isIncremental.not()) {
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
            }
        }
        jarFile.close()
        return isIncludeTarget
    }
    // directoryInput.changedFiles 中的文件只包含了改变的文件，真坑爹
    private fun scanDirectoryInput(directoryInput: DirectoryInput) {
        val dest = outputProvider.getContentLocation(
            directoryInput.name, directoryInput.contentTypes,
            directoryInput.scopes, Format.DIRECTORY
        )
        val map = directoryInput.changedFiles
        val dir = directoryInput.file
        if (isIncremental) {
            map.forEach { (file, status) ->
                val destFilePath = file.absolutePath.replace(dir.absolutePath, dest.absolutePath)
                val destFile = File(destFilePath)
                when (status) {
                    Status.NOTCHANGED -> {
                        scanClass(file)
                    }
                    Status.CHANGED, Status.ADDED -> {
                        try {
                            FileUtils.touch(destFile)
                        } catch (e: Exception) {
                            Files.createParentDirs(destFile)
                        }
                        scanClass(file)
                        if (file.isDirectory.not()) {
                            FileUtils.copyFile(file, destFile)
                        }
                    }
                    Status.REMOVED -> {
                        file.deleteRecursively()
                    }
                }
            }
        } else {
            scanClass(directoryInput.file)
            FileUtils.copyDirectory(directoryInput.file, dest)
        }
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