
plugins {
    id("java-gradle-plugin") // Java Gradle Plugin
    id("maven-publish")
    kotlin("jvm") version "1.8.0"
}

gradlePlugin {
    plugins.register("cs-plgin") {
        id = "com.brightk.cs"
        implementationClass = "com.k.plugin.CsPlugin"
    }
}

repositories {
    maven {
        credentials {
            username = "qzdapp"
            password = "Zhiyun123"
        }
        url = uri("http://maven.qizhidao.net:8081/repository/packages-app/app/android")
        isAllowInsecureProtocol = true
    }
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
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.brightk.cs"
            artifactId = "cs-plguin"
            version = "0.2.1"
            from(components["java"])
        }
    }
}
