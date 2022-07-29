package com.k.plugin.vistor.server


import com.k.plugin.AutoInjector
import com.k.plugin.CsServiceClassInfo
import com.k.plugin.vistor.OnAnnotationValueListener
import org.apache.http.util.TextUtils
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

class ServiceClassVisitor extends ClassVisitor {

    private String className
    private CsServiceClassInfo csServiceClassInfo

    private String[] interfaces

    public ServiceClassVisitor() {
        super(Opcodes.ASM6)
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.className = name
        csServiceClassInfo = null
        this.interfaces = interfaces
    }

    @Override
    AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor == AutoInjector.SERVICE_CS_URI) {
            OnAnnotationValueListener listener = new OnAnnotationValueListener() {
                @Override
                void onValue(String name, Object value) {
                    if (name == AutoInjector.SERVICE_CS_URI_FILE_NAME) {
                        if (TextUtils.isEmpty(value)) {
                            throw IllegalAccessException("${className}没有配置Url地址")
                        }
                        if (csServiceClassInfo == null) {
                            csServiceClassInfo = new CsServiceClassInfo(className, value)
                        }
                    }
                }
            }
            ServiceAnnotationVisitor annotationVisitor = new ServiceAnnotationVisitor(listener)
            return annotationVisitor
        } else {
            return super.visitAnnotation(descriptor, visible)
        }
    }

    @Override
    void visitEnd() {
        super.visitEnd()
        if (csServiceClassInfo != null) {
            if (interfaces.contains(AutoInjector.SERVICE_CS_CLASS)) {
                AutoInjector.csServiceClassInfoList.add(csServiceClassInfo)
            }
        }
    }
}
