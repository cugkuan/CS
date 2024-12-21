plugins {
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
    `signing`
}

group = "top.brightk"
version = "1.2.0"

java {
    withJavadocJar()
    withSourcesJar()
}
repositories {
    mavenCentral()
    google()
}


apply(from = "${rootDir.parent}/gradle/publish.gradle.kts")
val uploadRepository: Action<RepositoryHandler> by extra
publishing {
    publications {
        create<MavenPublication>("CsPlugin") {
            artifactId = "cs-annotation"
            groupId = project.group.toString()
            version = project.version.toString()
            from(components["java"])
            pom {
                // public maven must
                name.set("CS plugin")
                description.set("CS plugin,work for CS Annotation，注解，ksp，plugin使用")
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

