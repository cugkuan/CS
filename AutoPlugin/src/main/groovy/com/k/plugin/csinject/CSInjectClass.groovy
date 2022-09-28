


package com.k.plugin.csinject


import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * 代码注入带 CS 的init方法 中
 */
class CSInjectClass extends  ClassVisitor {
    private String className
    private String signature

    public CSInjectClass(ClassWriter writer) {
        super(Opcodes.ASM6, writer)
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
        this.signature = signature
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (name == "init"){
          InjectTargetMethodVisitor targetMethodVisitor =  new InjectTargetMethodVisitor(methodVisitor, access, name, descriptor)
            return targetMethodVisitor
        }
        return methodVisitor
    }
}
