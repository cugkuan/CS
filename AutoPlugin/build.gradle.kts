
plugins {
    id("java-gradle-plugin") // Java Gradle Plugin
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
}
group = "com.brightk.cs"
version = "1.0.8"

gradlePlugin {
    plugins {
        register("cs-plugin") {
            id = "com.brightk.cs"
            implementationClass = "com.k.plugin.CsPlugin"
        }
    }
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    gradleApi()
    localGroovy()
    implementation("com.android.tools.build:gradle:7.3.0")
    implementation("org.ow2.asm:asm:9.1")
    implementation("org.ow2.asm:asm-commons:9.1")
    implementation("com.android.tools:sdk-common:30.0.3")
    implementation("commons-io:commons-io:2.4")

}


publishing {
    repositories {
        maven {
            url = uri( "https://jitpack.io")
        }
    }
    publications {
        create<MavenPublication>("maven") {
            artifactId = "cs-plugin"
            from(components["java"])
        }
    }
}
