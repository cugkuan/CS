package com.k.plugin.csinterceptor

import com.k.plugin.InterceptorClassInfo
import com.k.plugin.CsPluginUtils
import com.k.plugin.Logger
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes


/**
 * 不进行合法性的校验，在使用的时候，已经进行了合法性的校验了
 */
internal class InterceptorClassVisitor(private val searchResultBack: (info: InterceptorClassInfo)->Unit) : ClassVisitor(Opcodes.ASM9) {
    private lateinit var className: String
    private var isTarget = false
    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<String>
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        isTarget = interfaces.contains(CsPluginUtils.FIND_INTERCEPTOR_CLASS_TARGET)
        className = name
    }

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
        return if (isTarget) InterceptorAnnotationVisitor(className,searchResultBack) else null
    }
}