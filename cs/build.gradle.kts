plugins {
    id("com.android.library")
    id("maven-publish")
    id("signing")
    alias(libs.plugins.kotlin.android)
}

android {
    compileSdk = 30
    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    namespace = "com.brightk.cs"
    kotlinOptions {
        jvmTarget = "11"
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}
group = "top.brightk"
version = "1.1.0"

dependencies {
    implementation("androidx.annotation:annotation:1.4.0")
    implementation(libs.core.ktx)
    api(libs.cs.annotation)
}

apply(from = "${rootProject.projectDir}/gradle/publish.gradle.kts")
val uploadRepository: Action<RepositoryHandler> by extra
publishing {
    publications {
        create<MavenPublication>("release") {
            artifactId = "cs"
            groupId = "top.brightk"
            version = project.version.toString()
            afterEvaluate {
                from(components["release"])
            }
            pom {
                name.set("CS")
                description.set("CS is a lightweight componentized framework, based on the idea of component-oriented service, supporting progressive transformation. It supports modular development, allowing developers to build applications more conveniently.\n" +
                        "\n" +
                        "CS 是一个轻量级的组件化框架，基于组件即服务的思想，支持渐进式改造。\n" +
                        "\n")
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

signing {
    sign(publishing.publications["release"])
}


