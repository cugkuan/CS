import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("java-gradle-plugin") // Java Gradle Plugin
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm") version "1.7.21"
    id("signing")
}
group = "top.brightk"
version = "1.0.9"

gradlePlugin {
    plugins {
        register("cs-plugin") {
            id = "top.brightk.cs"
            implementationClass = "com.k.plugin.CsPlugin"
        }
    }
}
repositories {
    mavenCentral()
    google()
}


tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allJava)
}

tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc.get().destinationDir)
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
ext {
    val file = rootProject.file("../local.properties")
    if (file.exists()) {
        val properties = Properties().apply {
            load(file.inputStream())
        }
        set("ossUsername", properties.getProperty("ossrhUsername"))
        set("ossPassword", properties.getProperty("ossrhPassword"))

        allprojects{
            extra["signing.keyId"] = properties.getProperty("signing.keyId")
            extra["signing.secretKeyRingFile"] = properties.getProperty("signing.password")
            extra["signing.password"] = properties.getProperty("signing.secretKeyRingFile")
        }
    }
}


publishing {
    repositories {
        maven {
            credentials {
                username = project.ext.get("ossUsername") as String
                password = project.ext.get("ossPassword") as String
            }
            val publicUrl = if (version.toString().endsWith("SNAPSHOT")) {
                "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            } else {
                "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            }
            url = uri(publicUrl)
            group = "top.brightk"
        }
    }
    publications {
        create<MavenPublication>("pluginCS") {
            artifactId = "cs-plugin"
            groupId = "top.brightk"
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
            pom {
                // public maven must
                name.set("CS plugin")
                description.set("CS plugin,work for CS;CS插件，服务拦截器自动注册")
                val pomUrl = "https://github.com/cugkuan/CS"
                val pomScm = "https://github.com/cugkuan/CS.git"
                url.set(pomUrl)
                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("BrightK")
                        name.set("BrightK")
                        email.set("cugkuan@163.com")
                    }
                }
                scm {
                    connection.set(pomUrl)
                    developerConnection.set(pomScm)
                    url.set(pomUrl)
                }
            }
        }
    }
}

afterEvaluate {
    signing {
        sign(publishing.publications)
    }
}

