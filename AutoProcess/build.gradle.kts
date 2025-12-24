import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java-library")
    alias(libs.plugins.publish)
}

group = "top.brightk"
version = "1.1.0"

java {
    withJavadocJar()
    withSourcesJar()
}

dependencies{
    implementation(libs.ksp.symbol.processing.api)
    implementation(libs.cs.annotation)
    implementation(libs.gson)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
    coordinates("top.brightk", "cs-process",
        project.version.toString())

    pom {
        name.set("CS KSP Processor")
        description.set("CS ksp,work for CS;Ksp插件，使用ksp加快编译速度")
        url.set("https://github.com/cugkuan/CS")
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
            url.set("https://github.com/cugkuan/CS")
            connection.set("scm:git:git://github.com/cugkuan/CS.git")
            developerConnection.set("scm:git:ssh://git@github.com/cugkuan/CS.git")
        }
    }
}
