
plugins {
    id("com.android.application") version "7.3.0" apply false
    id("com.android.library") version "7.3.0" apply false
    id("com.brightk.cs") apply false
    id("org.jetbrains.kotlin.jvm") version "1.7.21" apply false
    id("com.google.devtools.ksp") version "1.7.21-1.0.8" apply false
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}



