val kspVersion: String by project

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

group = "com.brightk.cs"
version = "1.0-SNAPSHOT"


dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup:javapoet:1.12.1")
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
    api(project(":CsAnnotation"))
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

