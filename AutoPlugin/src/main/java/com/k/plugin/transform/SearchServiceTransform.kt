package com.k.plugin.transform

import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.ListProperty
import java.io.InputStream
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.ForkJoinPool
import java.util.jar.JarFile

/**
 * 这一步处理了增量更新了
 */
class SearchServiceTransform(
    private val allJars: ListProperty<RegularFile>,
    private val allDirectories: ListProperty<Directory>,
    private val searchCallBack: (classBytes: ByteArray) -> Unit,
) {
    private val filter = DefaultClassNameFilter
    private val executor: ExecutorService = ForkJoinPool.commonPool()
    private val tasks: MutableList<Callable<Int>> = ArrayList()
    fun startTransform() {
        tasks.clear()
        allJars.get().forEach { regularFile ->
            println("JarFile:${regularFile.asFile.absolutePath}")
            val task = Callable {
                JarFile(regularFile.asFile).use { jarFile ->
                    jarFile.entries().asIterator().forEach iterator@{ jarEntry ->
                        if (filter.filter(jarEntry.name).not()) {
                            scan(jarFile.getInputStream(jarEntry))
                        }
                    }
                }
                0
            }
            tasks.add(task)
        }
        allDirectories.get().forEach { directory ->
            val directoryFile = directory.asFile
            val task = Callable {
                directoryFile.walk()
                    .forEach iterator@{ file ->
                        if (file.isDirectory) return@iterator
                        if (filter.filter(file.name).not()) {
                            process(file.inputStream().readAllBytes())
                        }
                    }
                0
            }
            tasks.add(task)
        }
        executor.invokeAll(tasks)
    }

    private fun scan(inputStream: InputStream) {
        inputStream.use { stream ->
            stream.readAllBytes().takeIf { it.isNotEmpty() }?.let { byteArr ->
                process(byteArr)
            }
        }
    }

    private fun process(classBytes: ByteArray) {
        searchCallBack.invoke(classBytes)
    }
}