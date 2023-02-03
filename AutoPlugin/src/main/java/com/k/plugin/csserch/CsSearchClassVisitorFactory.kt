package com.k.plugin.csserch

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.k.plugin.CsPluginUtils
import org.objectweb.asm.ClassVisitor


abstract class CsSearchClassVisitorFactory :
    AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return ServiceClassVisitor(nextClassVisitor)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return classData.interfaces.contains(CsPluginUtils.FIND_SERVICE_CLASS_TARGET)
    }
}