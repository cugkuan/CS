val kspVersion: String by project

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("maven-publish")
}

group = "com.brightk.cs"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup:javapoet:1.12.1")
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
    implementation(project(":CsAnnotation"))
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}


publishing.repositories {
    maven {
        // 配置地址
        credentials {
            username = "qzdapp"
            password = "Zhiyun123"
        }
        url = uri("http://maven.qizhidao.net:8081/repository/packages-app/app/android")
        isAllowInsecureProtocol = true
    }
}

val sourceJar:TaskProvider<Jar> by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(project.the<SourceSetContainer>()["main"].allSource)
}

artifacts {
    sourceJar
}


publishing {
    publications {
        create<MavenPublication>("processor") {
            groupId = "com.brightk.cs"
            artifactId = "cs-processor"
            version = "0.1.3"
            from(components["java"])
        }
    }
}
