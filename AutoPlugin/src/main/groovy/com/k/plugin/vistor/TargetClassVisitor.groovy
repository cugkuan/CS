package com.k.plugin.vistor


import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class TargetClassVisitor extends ClassVisitor {
    private String className
    private String signature
     public  TargetClassVisitor(ClassWriter writer) {
        super(Opcodes.ASM6,writer)
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
        TargetMethodVisitor targetMethodVisitor = new TargetMethodVisitor(methodVisitor,access,name,descriptor)
        return  targetMethodVisitor
    }
}
