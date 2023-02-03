package com.k.plugin.csserch

import com.k.plugin.CsPluginUtils
import com.k.plugin.CsServiceClassInfo
import com.k.plugin.Logger
import org.gradle.internal.impldep.org.apache.http.util.TextUtils
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Opcodes

/**
 * flag 跟库里面的约定保持一致性
 */
internal class ServiceAnnotationVisitor(private val className: String) :
    AnnotationVisitor(Opcodes.ASM6) {
    private var url: String? = null
    private var flag = 'a'
    override fun visit(name: String, value: Any) {
        super.visit(name, value)
        if (name == CsPluginUtils.SERVICE_CS_URI_FILE_NAME) {
            if ((value as? String).isNullOrEmpty()) {
                throw IllegalAccessException("\${className}没有配置Url地址")
            } else {
                url = value as String
            }
        }
    }
    override fun visitEnum(name: String, descriptor: String, value: String) {
        super.visitEnum(name, descriptor, value)
        if (name == CsPluginUtils.SERVICE_CS_URI_FILE_TYPE) {
            flag = when (value) {
                "SINGLE" -> {
                    'b'
                }
                "NEW" -> {
                    'c'
                }
                else -> {
                    'a'
                }
            }
        }
    }
    override fun visitEnd() {
        super.visitEnd()
        url?.let { url ->
            Logger.error("$className==>$url")
            val c = className + flag
            val classInfo = CsServiceClassInfo(c, url)
            CsPluginUtils.csServiceClassInfoList.add(classInfo)
        }
    }
}