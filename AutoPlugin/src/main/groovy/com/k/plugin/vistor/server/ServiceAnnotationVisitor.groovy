package com.k.plugin.vistor.server

import com.k.plugin.AutoInjector
import com.k.plugin.CsServiceClassInfo
import com.k.plugin.Logger
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Opcodes

/**
 * flag 跟库里面的约定保持一致性
 */
class ServiceAnnotationVisitor extends AnnotationVisitor {

    private String className
    private String url
    private char flag = 'a'
    ServiceAnnotationVisitor(String className) {
        super(Opcodes.ASM6)
        this.className = className
    }

    @Override
    void visit(String name, Object value) {
        super.visit(name, value)
        Logger.error(name+"==>"+value)
        if (name == AutoInjector.SERVICE_CS_URI_FILE_NAME) {
            if (TextUtils.isEmpty(value)) {
                throw IllegalAccessException("${className}没有配置Url地址")
            }else {
                url = value
            }
        }
    }


    @Override
    void visitEnum(String name, String descriptor, String value) {
        super.visitEnum(name, descriptor, value)
        if (name == AutoInjector.SERVICE_CS_URI_FILE_TYPE){
            if (value == "SINGLE"){
                flag = 'b'
            }else if (value == 'NEW'){
                flag = 'c'
            }else {
                flag = 'a'
            }

        }
    }

    @Override
    void visitEnd() {
        super.visitEnd()
        String c = className+flag
        CsServiceClassInfo classInfo = new CsServiceClassInfo(c,url)
        AutoInjector.csServiceClassInfoList.add(classInfo)
        Logger.error(c +"==>"  +url)

    }
}
