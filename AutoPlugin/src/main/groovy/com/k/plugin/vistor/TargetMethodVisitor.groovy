package com.k.plugin.vistor

import com.k.plugin.AutoInjector
import com.k.plugin.Logger
import com.k.plugin.TargetClassInfo
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class TargetMethodVisitor extends MethodVisitor {

    private boolean needInject = false
    public TargetMethodVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM6, methodVisitor)
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor == AutoInjector.AUTO_REGISTER_TARGET) {
            TargetClassInfo targetClassInfo = new TargetClassInfo()
            AutoInjector.targetClassInfo = targetClassInfo
            needInject = true
            Logger.error("找到了目标")
        }
        Logger.error(descriptor)
        return super.visitAnnotation(descriptor, visible);
    }


    @Override
    void visitCode() {
        if (needInject) {
            Logger.error('______需要改写代码___________')
            if (mv == null){
                Logger.error('mv 是null的，真奇怪')
            }
        }
        if (mv == null) {
            Logger.error('______null被还原___________')
            super.visitCode()
            return
        }
        if (!needInject) {
            mv.visitCode()
            return
        }
        Logger.error('______开始插装___________')
       mv.visitLdcInsn("lmk")
        Logger.error('______1___________')
       mv.visitLdcInsn("t")
        Logger.error('______2___________')
       mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false)
        Logger.error('______3___________')
       mv.visitInsn(Opcodes.POP)
        Logger.error('______4___________')
       mv.visitInsn(Opcodes.RETURN)
        Logger.error('______5___________')
       mv.visitMaxs(2, 1)
        Logger.error('______6___________')
//        mv.visitFieldInsn(Opcodes.GETSTATIC, "com/k/cs/CS", "INSTANCE", "Lcom/k/cs/CS;");
//        mv.visitLdcInsn("key");
//        mv.visitLdcInsn(Type.getType("Lcom/k/app2/App2Service;"));
//        mv.visitMethodInsn(INVOKEVIRTUAL, "com/k/cs/CS", "register", "(Ljava/lang/String;Ljava/lang/Class;)V", false);
//        mv.visitInsn(RETURN);
//        mv.visitMaxs(3, 1);

//        Logger.error('______----------___________')


//        AutoInjector.csServiceClassInfoList.forEach{ CsServiceClassInfo info  ->
//
//            mv.visitFieldInsn(Opcodes.GETSTATIC,"com/k/cs/CS",Opcodes.INSTANCEOF)
//
//        }

        super.visitCode()

    }
}
