plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java-library")
    `maven-publish`
    `signing`
}

group = "top.brightk"
version = "1.0.3"

java {
    withJavadocJar()
    withSourcesJar()
}
//repositories {
//    mavenCentral()
//    google()
//}
dependencies{
    implementation(libs.ksp.symbol.processing.api)
    implementation(libs.cs.annotation)
    implementation(libs.gson)
}
try {
    apply(from = "${rootProject.rootDir.parent}/gradle/publish.gradle.kts")
}catch (e:Exception){
    apply(from = "${rootDir}/gradle/publish.gradle.kts")
}

val uploadRepository: Action<RepositoryHandler> by extra
publishing {
    publications {
        create<MavenPublication>("CsPlugin") {
            artifactId = "cs-process"
            groupId = project.group.toString()
            version = project.version.toString()
            from(components["java"])
            pom {
                // public maven must
                name.set("CS plugin")
                description.set("CS ksp,work for CS;Ksp插件，使用ksp加快编译速度")
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

afterEvaluate {
    signing {
        sign(publishing.publications)
    }
}

