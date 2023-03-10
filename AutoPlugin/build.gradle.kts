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
    }
}


publishing {
    repositories {
        maven {
            credentials {
                username = project.ext.get("ossUsername") as String
                password = project.ext.get("ossPassword") as String

                logger.warn("$username --- $password")
            }
            val publicUrl = if ((version as? String)?.endsWith("SNAPSHOTS") == true) {
                "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            } else {
                "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            }
            url = uri(publicUrl)
            group = "top.brightk"
        }
    }
    publications {
        create<MavenPublication>("maven") {
            artifactId = "cs-plugin"
            groupId = "top.brightk"
            afterEvaluate {
                from(components["java"])
            }
            pom {
                signing {
                    sign(configurations.archives.get())
                }
                // public maven must
                name.set("CS plugin")
                description.set("CS plugin,work for CS;CS插件，服务拦截器自动注册")
                url.set(rootProject.properties["POM_URL"] as? String)
                licenses {
                    license {
                        name.set(rootProject.properties["POM_LICENCE_NAME"] as? String)
                        url.set(rootProject.properties["POM_LICENCE_URL"] as? String)
                    }
                }
                developers {
                    developer {
                        id.set(rootProject.properties["AUTHOR_NAME"] as? String)
                        name.set(rootProject.properties["AUTHOR_NAME"] as? String)
                        email.set(rootProject.properties["POM_EMAIL"] as? String)
                    }
                }
                scm {
                    connection.set(rootProject.properties["POM_URL"] as? String)
                    developerConnection.set(rootProject.properties["POM_SCM"] as? String)
                    url.set(rootProject.properties["POM_URL"] as? String)
                }
            }
        }
    }
}

