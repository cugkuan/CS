import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
}


subprojects {
    plugins.whenPluginAdded {
        when(this){
            is AppPlugin ->{
                applyKspPluginAndDependencies()
            }
            is LibraryPlugin ->{
                if (name != "cs"){
                    applyKspPluginAndDependencies()
                }
            }
        }
    }
}

fun Project.applyKspPluginAndDependencies() {
    plugins.apply("com.google.devtools.ksp")
    dependencies.apply {
        add("implementation", libs.ksp.symbol.processing.api)
        add("implementation",(project(":AutoProcess")))
        add("ksp",(project(":AutoProcess")))
    }
    extensions.findByType(AndroidComponentsExtension::class.java)?.apply {
        onVariants { variant->
            kotlinExtension.sourceSets.findByName(variant.name)?.kotlin?.srcDirs(
                file("${layout.buildDirectory}/generated/ksp/${variant.name}/kotlin")
            )
        }
    }
}