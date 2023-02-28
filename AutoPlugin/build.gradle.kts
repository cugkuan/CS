
plugins {
    id("java-gradle-plugin") // Java Gradle Plugin
    id("maven-publish")
    kotlin("jvm") version "1.8.0"
}
group = "com.brightk.cs"
version = "1.0.1"

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
            credentials {
                username = "qzdapp"
                password = "Zhiyun123"
            }
            url = uri("http://maven.qizhidao.net:8081/repository/packages-app/app/android")
            isAllowInsecureProtocol = true
        }
    }
    publications {
        create<MavenPublication>("maven") {
            artifactId = "cs-plugin"
            from(components["java"])
        }
    }
}
