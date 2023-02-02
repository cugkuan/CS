plugins {
    id("com.android.application") version "7.3.0" apply false
    id("com.android.library") version "7.3.0" apply false
    id("com.brightk.cs") apply false
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}



