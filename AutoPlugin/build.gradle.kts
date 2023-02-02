plugins {
    id("groovy") // Groovy Language
    id("java-gradle-plugin") // Java Gradle Plugin
    id("maven-publish")
}

gradlePlugin {
    plugins.register("cs-plgin") {
        id = "com.brightk.cs"
        implementationClass = "com.k.plugin.CsAutoPlugin"
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
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.brightk.cs"
            artifactId = "cs-plguin"
            version = "0.1.0"
            from(components["java"])
        }
    }
}
