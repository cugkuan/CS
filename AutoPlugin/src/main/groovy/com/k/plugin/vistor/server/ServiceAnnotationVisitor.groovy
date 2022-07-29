package com.k.plugin.vistor.server

import com.k.plugin.vistor.OnAnnotationValueListener
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Opcodes

class ServiceAnnotationVisitor extends AnnotationVisitor {

    OnAnnotationValueListener listener
    ServiceAnnotationVisitor(OnAnnotationValueListener onAnnotationValueListener) {
        super(Opcodes.ASM6)
        this.listener = onAnnotationValueListener
    }

    @Override
    void visit(String name, Object value) {
        super.visit(name, value)
        listener.onValue(name, value)
    }
}
