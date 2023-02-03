package com.k.plugin.csserch

import com.k.plugin.CsPluginUtils
import com.k.plugin.CsServiceClassInfo
import com.k.plugin.Logger
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes


/**
 * 不进行合法性的校验，在使用的时候，已经进行了合法性的校验了
 */
internal class ServiceClassVisitor : ClassVisitor {
    constructor( classVisitor: ClassVisitor):super(Opcodes.ASM9,classVisitor)
    private lateinit var className: String
    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String,
        interfaces: Array<String>
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
    }

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
        return ServiceAnnotationVisitor(className)
    }
}