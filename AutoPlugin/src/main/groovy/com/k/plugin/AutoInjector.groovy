package com.k.plugin

import com.k.plugin.vistor.OnInjectListener
import com.k.plugin.vistor.TargetClassVisitor
import com.k.plugin.vistor.server.ServiceClassVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

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
    public static List<CsServiceClassInfo> csServiceClassInfoList = new ArrayList<>()
    public static ServiceClassVisitor serviceClassVisitor = new ServiceClassVisitor()


    static boolean isFinishInject = false


    static clear() {
        csServiceClassInfoList.clear()
        isFinishInject = false
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
    /**
     * 代码自动注入中.............
     * @param bytes
     * @return
     */
    private static byte[] findTargetAndInject(byte[] bytes) {
        boolean methodInject
        OnInjectListener onMethodInjectListener = new OnInjectListener() {
            @Override
            void inject() {
                methodInject = true
                isFinishInject = true
            }
        }
        try {
            ClassReader classReader = new ClassReader(bytes)
            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS)

            TargetClassVisitor targetClassVisitor = new TargetClassVisitor(classWriter)
            targetClassVisitor.setOnInjectListener(onMethodInjectListener)
            classReader.accept(targetClassVisitor, ClassReader.EXPAND_FRAMES)

            if (methodInject) {
                return classWriter.toByteArray()
            } else {
                return null
            }
        } catch (Exception e) {

        }
        return null
    }


    static void registerTargetClassInfo(File source) {
        if (isFinishInject) {
            return
        }
        if (source.isDirectory()) {
            source.eachFileRecurse { File file ->
                if (isFinishInject) {
                    return
                }
                String fileName = file.getName()
                if (filterClass(fileName)) {
                    return
                }
                byte[] code = findTargetAndInject(file.bytes)
                if (code != null) {
                    FileOutputStream fos = new FileOutputStream(file)
                    fos.write(code)
                    fos.close()
                }
            }
        } else {
            if (isFinishInject) {
                return
            }
            //JarFile文件的处理规则
            Map<String, byte[]> tempModifiedClassByteMap = new HashMap()
            JarFile jarFile = new JarFile(source)
            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries()
            while (jarEntryEnumeration.hasMoreElements()) {
                if (isFinishInject) {
                    break
                }
                JarEntry jarEntry = jarEntryEnumeration.nextElement()
                String filename = jarEntry.getName()
                if (filterPackage(filename)) {
                    break
                }
                if (filterClass(filename)) {
                    continue
                }
                InputStream inputStream = jarFile.getInputStream(jarEntry)
                if (inputStream != null) {
                    byte[] bytes = findTargetAndInject(inputStream.bytes)
                    if (bytes != null) {
                        tempModifiedClassByteMap.put(filename, bytes)
                    }
                }
                inputStream.close()
            }

            if (tempModifiedClassByteMap.size() != 0) {
                File tempJar = new File(source.absolutePath.replace('.jar', 'temp.jar'))
                if (tempJar.exists()) {
                    tempJar.delete()
                }
                jarEntryEnumeration = jarFile.entries()
                JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tempJar))
                while (jarEntryEnumeration.hasMoreElements()) {
                    JarEntry jarEntry = jarEntryEnumeration.nextElement()
                    String filename = jarEntry.getName()
                    ZipEntry zipEntry = new ZipEntry(filename)
                    jarOutputStream.putNextEntry(zipEntry)
                    if (tempModifiedClassByteMap.containsKey(filename)) {
                        jarOutputStream.write(tempModifiedClassByteMap.get(filename))
                    } else {
                        InputStream inputStream = jarFile.getInputStream(jarEntry)
                        jarOutputStream.write(inputStream.bytes)
                        inputStream.close()
                    }
                    jarOutputStream.closeEntry()
                }
                jarOutputStream.close()
                FileOutputStream outputStream = new FileOutputStream(source)
                outputStream.write(tempJar.bytes)
                outputStream.close()
                tempJar.delete()
            }
            jarFile.close()
        }
    }


}
