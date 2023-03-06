plugins {
    id("com.android.library")
    id("maven-publish")
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
group = "com.brightk.cs"
version = "1.0.0"
dependencies {
    implementation("androidx.annotation:annotation:1.4.0")
}

publishing {
    repositories {
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
    publications {
        create<MavenPublication>("release") {
            artifactId = "cs"
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}


