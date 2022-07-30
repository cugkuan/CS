package com.k.plugin


import com.k.plugin.vistor.TargetClassVisitor
import com.k.plugin.vistor.server.ServiceClassVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

import java.util.jar.JarEntry
import java.util.jar.JarFile

class AutoInjector {
    /**
     * 服务地址的配置注解
     */
    public static String SERVICE_CS_URI = "Lcom/cugkuan/cs/core/annotation/CsUri;"
    /**
     * 服务注解配置的uri
     */
    public static String SERVICE_CS_URI_FILE_NAME = "uri"
    /**
     * 继承的服务
     */
    public static String SERVICE_CS_CLASS = "com/cugkuan/cs/core/CService"
    /**
     * 自动注入的目标
     */
    public static final String AUTO_REGISTER_TARGET = "Lcom/cugkuan/cs/core/annotation/AutoRegisterTarget;"


    public static String[] ignorePackages
    public static TargetClassInfo targetClassInfo
    public static List<CsServiceClassInfo> csServiceClassInfoList = new ArrayList<>()
    public static ServiceClassVisitor serviceClassVisitor = new ServiceClassVisitor()


    static clear() {
        targetClassInfo = null
        csServiceClassInfoList.clear()
    }

    static verify() {
        if (targetClassInfo == null) {
            Logger.error("没有找到对应的注入地方")
        } else {
            Logger.error("target ---->${targetClassInfo.className} -- ${targetClassInfo.method}")

        }
        csServiceClassInfoList.forEach { CsServiceClassInfo classInfo ->
            Logger.error("service ---> ${classInfo.className} -- ${classInfo.url} -- ${classInfo.urlKey}")
        }
    }


    static boolean filterClass(String filename) {
        if (!filename.endsWith(".class")
                || filename.contains('R$')
                || filename.contains('R.class')
                || filename.contains("BuildConfig.class")) {
            return true
        }
        return false
    }

    public static boolean filterPackage(String filename) {
        if (ignorePackages == null) return false
        for (int i = 0; i < ignorePackages.size(); i++) {
            if (filename.startsWith(ignorePackages[i])) {
                return true
            }
        }
        return false
    }
    /**
     * 找到关联的服务对象
     * @param source
     */
    static void findServiceClassInfo(File source) {
        if (source.isDirectory()) {
            source.eachFileRecurse { File file ->
                String fileName = file.getName()
                if (filterClass(fileName)) {
                    return
                }
                ClassReader classReader = new ClassReader(file.readBytes())
                classReader.accept(serviceClassVisitor, 0)
            }
        } else {
            JarFile jarFile = new JarFile(source)
            Enumeration<JarEntry> entries = jarFile.entries()
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement()
                String filename = entry.getName()
                if (filterPackage(filename)) break
                if (filterClass(filename)) continue
                InputStream stream = jarFile.getInputStream(entry)
                if (stream != null) {
                    ClassReader classReader = new ClassReader(stream.bytes)
                    classReader.accept(serviceClassVisitor, 0)
                    stream.close()
                }
            }
            jarFile.close()
        }
    }


    static void registerTargetClassInfo(File source) {
        if (targetClassInfo != null) {
            return
        }
        if (source.isDirectory()) {
            source.eachFileRecurse { File file ->
                if (targetClassInfo != null) {
                    return
                }
                String fileName = file.getName()
                if (filterClass(fileName)) {
                    return
                }
                ClassReader classReader = new ClassReader(file.bytes)
                ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS)

                TargetClassVisitor targetClassVisitor = new TargetClassVisitor(classWriter)
                classReader.accept(targetClassVisitor, ClassReader.EXPAND_FRAMES)
                byte[] code = classWriter.toByteArray()
                FileOutputStream fos = new FileOutputStream(file)

                fos.write(code)
                fos.close()
            }
        } else {
//            if (targetClassInfo != null) {
//                return
//            }
//
//            JarFile jarFile = new JarFile(source)
//            Enumeration<JarEntry> entries = jarFile.entries()
//            while (entries.hasMoreElements()) {
//                if (targetClassInfo != null) {
//                    return
//                }
//                JarEntry entry = entries.nextElement()
//                String filename = entry.getName()
//                if (filterPackage(filename)) break
//                if (filterClass(filename)) continue
//                InputStream stream = jarFile.getInputStream(entry)
//                if (stream != null) {
//                    ClassReader classReader = new ClassReader(stream.bytes)
//                    classReader.accept(targetClassVisitor, 0)
//                    stream.close()
//                }
//            }
//            jarFile.close()

        }
    }


}
