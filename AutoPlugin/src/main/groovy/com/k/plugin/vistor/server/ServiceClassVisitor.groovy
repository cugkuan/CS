package com.k.plugin.vistor.server

import com.k.plugin.AutoInjector
import com.k.plugin.CsServiceClassInfo
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class ServiceClassVisitor extends ClassNode {
    private String className
    private CsServiceClassInfo csServiceClassInfo
    private boolean isService;
    public ServiceClassVisitor() {
        super(Opcodes.ASM6)
    }
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.className = name
        csServiceClassInfo = null
        isService = interfaces.contains(AutoInjector.SERVICE_CS_CLASS)
    }
    @Override
    AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (isService && descriptor == AutoInjector.SERVICE_CS_URI) {
            return new ServiceAnnotationVisitor(className)
        } else {
            return super.visitAnnotation(descriptor, visible)
        }
    }

    @Override
    void visitEnd() {
        super.visitEnd()
    }
}
