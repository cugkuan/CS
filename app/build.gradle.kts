plugins {
    id("com.android.application")
    id("com.brightk.cs")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.android")
}

csConfig {
    scanPackages = arrayOf("com/demo")
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 23
        minSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation(project(":test1"))
    implementation(project(":test2"))
    implementation(project(":cs"))

    implementation(project(":CsProcessor"))
    ksp(project(":CsProcessor"))


}