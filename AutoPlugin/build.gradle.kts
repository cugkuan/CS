plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    `signing`
}
val publicGroup = "top.brightk"
val publicVersion = "3.0.0"
val publicArtifact = "cs-plugin"

group = publicGroup
version = publicVersion

java {
    withJavadocJar()
    withSourcesJar()
}
repositories {
    mavenCentral()
    google()
}
dependencies {
    implementation(libs.gradle.kotlin)
    implementation(libs.gradle)
    // asm
    implementation(libs.asm.core)
    implementation(libs.asm.commons)
    implementation(libs.asm.util)
    implementation(libs.asm.tree)
    implementation(libs.asm.analysis)
}

apply(from = "${rootDir.parent}/gradle/publish.gradle.kts")
val uploadRepository: Action<RepositoryHandler> by extra
publishing {
    publications {
        create<MavenPublication>("CsPlugin") {
            artifactId = publicArtifact
            groupId = publicGroup
            version = publicVersion
            from(components["java"])
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
    repositories(uploadRepository)
}
gradlePlugin {
    plugins {
        register("cs-plugin") {
            id = "top.brightk.cs"
            group = publicGroup
            implementationClass = "com.k.plugin.CsPlugin"
        }
    }
}
afterEvaluate {
    signing {
        sign(publishing.publications)
    }
}

