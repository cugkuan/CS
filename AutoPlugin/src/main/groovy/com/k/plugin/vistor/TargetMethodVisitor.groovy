package com.k.plugin.vistor

import com.k.plugin.AutoInjector
import com.k.plugin.Logger
import com.k.plugin.TargetClassInfo
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class TargetMethodVisitor extends AdviceAdapter{

    private boolean needInject = false
    private OnInjectListener onInjectListener

    private String name;

    public TargetMethodVisitor(MethodVisitor methodVisitor,int access,String name,String descriptor) {
        super(Opcodes.ASM6, methodVisitor,access,name,descriptor)
        this.name = name
    }

    void setOnInjectListener(OnInjectListener listener){
        onInjectListener = listener
    }


    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor == AutoInjector.AUTO_REGISTER_TARGET) {
            needInject = true
            if (onInjectListener != null){
                onInjectListener.inject()
                Logger.error("CsAutoInject将代码注入----> ${this.name}")
            }
        }
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode)
        if (needInject) {
            mv.visitCode();
            mv.visitLdcInsn("lmk");
            mv.visitLdcInsn("a");
            mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            mv.visitInsn(POP);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd()
        }
    }
}
