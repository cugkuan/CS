import java.util.Properties

plugins {
    id("com.android.application") version "7.3.0" apply false
    id("com.android.library") version "7.3.0" apply false
    id("top.brightk.cs") apply false
    kotlin("jvm") version "1.7.21" apply false
}


buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}


ext {
    val file = rootProject.file("local.properties")
    if (file.exists()){
        val properties = Properties().apply {
            load(file.inputStream())
        }
        set("ossUsername",properties.getProperty("ossrhUsername"))
        set("ossPassword",properties.getProperty("ossrhPassword"))
    }
}





