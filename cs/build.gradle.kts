plugins {
    id("com.android.library")
    id("maven-publish")
    id("signing")
}

android {
    compileSdk = 30
    defaultConfig {
        minSdk = 21
        minSdk = 31
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
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}
group = "top.brightk"
version = "1.0.0"

dependencies {
    implementation("androidx.annotation:annotation:1.4.0")
}


publishing {
    repositories {
        maven {
            credentials {
                username = rootProject.ext.get("ossUsername") as String
                password = rootProject.ext.get("ossPassword") as String
            }
            val publicUrl = if ((version as? String)?.endsWith("SNAPSHOTS") == true) {
                "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            } else {
                "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            }
            url = uri(publicUrl)
        }
    }
    publications {
        create<MavenPublication>("release") {
            artifactId = "cs"
            groupId = "top.brightk"
            afterEvaluate {
                from(components["release"])
            }
            pom {
                signing {
                    sign(publishing.publications["release"])
                }

                name.set("CS")
                description.set(rootProject.properties["POM_DES"] as? String)
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
                    url.set(rootProject.properties["POM_SCM"] as? String)
                }
            }
        }
    }
}


