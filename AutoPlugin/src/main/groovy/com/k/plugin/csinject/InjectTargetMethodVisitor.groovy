package com.k.plugin.csinject

import com.k.plugin.AutoInjector
import com.k.plugin.CsServiceClassInfo
import com.k.plugin.Logger
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class InjectTargetMethodVisitor extends AdviceAdapter {
    public InjectTargetMethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(Opcodes.ASM6, methodVisitor, access, name, descriptor)
    }
    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode)
        Logger.error("服务的个数:${AutoInjector.csServiceClassInfoList.size()}")
        mv.visitCode()
        for (int i = 0; i < AutoInjector.csServiceClassInfoList.size(); i++) {
            CsServiceClassInfo info = AutoInjector.csServiceClassInfoList.get(i)
            mv.visitLdcInsn(info.urlKey)
            mv.visitVarInsn(ASTORE, 1)
            mv.visitLdcInsn(info.className)
            mv.visitVarInsn(ASTORE, 2)
            mv.visitVarInsn(ALOAD, 1)
            mv.visitVarInsn(ALOAD, 2)
            mv.visitMethodInsn(INVOKESTATIC, "com/brightk/cs/CsPluginRegister", "register", "(Ljava/lang/String;Ljava/lang/String;)V", false)
        }
        mv.visitInsn(RETURN);
        mv.visitMaxs(3, 3);
        mv.visitEnd()

    }
}
