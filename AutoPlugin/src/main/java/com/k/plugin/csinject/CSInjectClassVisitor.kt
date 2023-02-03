package com.k.plugin.csinject

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * 代码注入带 CS 的init方法 中
 * 使用的时间验证合法性
 */
class CSInjectClassVisitor : ClassVisitor {
    private var className: String? = null
    private var signature: String? = null
    constructor(classVisitor: ClassVisitor?) : super(Opcodes.ASM9, classVisitor)
    constructor(writer: ClassWriter?) : super(Opcodes.ASM9, writer)
    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
        this.signature = signature
    }
    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String?,
        signature: String?,
        exceptions: Array<String>?
    ): MethodVisitor? {
        val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        return if (name == "init") {
            InjectTargetMethodVisitor(methodVisitor, access, name, descriptor)
        } else methodVisitor
    }
}