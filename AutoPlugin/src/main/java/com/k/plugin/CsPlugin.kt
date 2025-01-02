package com.k.plugin


import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationParameters
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.k.plugin.csinject.CSInjectClassVisitor
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor

abstract class CsPlugin :Plugin<Project> {
    override fun apply(project: Project) {
        Logger.project = project
        val android = project.extensions.getByType(AndroidComponentsExtension::class.java)
        android.onVariants {  variant ->
            variant.instrumentation.transformClassesWith(
                InjectClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) {}
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
            variant.instrumentation.excludes.addAll(
                "android/**",
                "androidx/**",
                "com/google/**",
                "kotlin/**",
                "kotlinx/**",
                "java/**",
                "javax/**",
                "apple/**",
                "jdk/**"
            )
        }
    }
}

abstract class InjectClassVisitorFactory :
    AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return CSInjectClassVisitor(classContext,nextClassVisitor)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return classData.className == "com.brightk.cs.CS"
    }

}

