package com.k.plugin.csinject

import com.k.plugin.CsServiceClassInfo
import com.k.plugin.Logger
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

internal class InjectTargetMethodVisitor(
    methodVisitor: MethodVisitor?,
    access: Int,
    name: String?,
    descriptor: String?,
    private val services:List<CsServiceClassInfo>
) : AdviceAdapter(
    ASM9, methodVisitor, access, name, descriptor
) {
    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)
        mv.visitCode()
        services.forEach{ info ->
            mv.visitLdcInsn(info.urlKey)
            mv.visitVarInsn(ASTORE, 1)
            mv.visitLdcInsn(info.className)
            mv.visitVarInsn(ASTORE, 2)
            mv.visitVarInsn(ALOAD, 1)
            mv.visitVarInsn(ALOAD, 2)
            mv.visitMethodInsn(
                INVOKESTATIC,
                "com/brightk/cs/CsPluginRegister",
                "register",
                "(Ljava/lang/String;Ljava/lang/String;)V",
                false
            )
        }
        mv.visitInsn(RETURN)
        mv.visitMaxs(3, 3)
        mv.visitEnd()
        Logger.error("服务注册完成")
    }
}