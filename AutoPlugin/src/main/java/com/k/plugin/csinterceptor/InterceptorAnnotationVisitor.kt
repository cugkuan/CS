package com.k.plugin.csinterceptor

import com.k.plugin.InterceptorClassInfo
import com.k.plugin.CsPluginUtils
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Opcodes
import kotlin.properties.Delegates

/**
 * flag 跟库里面的约定保持一致性
 */
internal class InterceptorAnnotationVisitor(private val className: String, private val searchResultBack: (info: InterceptorClassInfo)->Unit) :
    AnnotationVisitor(Opcodes.ASM6) {
    private lateinit var  interceptorName: String
    private var priority by Delegates.notNull<Int>()
    override fun visit(name: String, value: Any) {
        super.visit(name, value)
        if (name == CsPluginUtils.INTERCEPTOR_NAME) {
            if ((value as? String).isNullOrEmpty()) {
                throw IllegalAccessException("${className}没有配置 name")
            } else {
                interceptorName = value as String
            }
        }else if (name == CsPluginUtils.INTERCEPTOR_PRIORITY){
            priority = if ((value as? Int) != null){
                value
            }else{
                Int.MAX_VALUE
            }
        }
    }
    override fun visitEnd() {
        super.visitEnd()
        val classInfo = InterceptorClassInfo(
            className,
            priority,
            interceptorName
        )
        searchResultBack.invoke(classInfo)

    }
}