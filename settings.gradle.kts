rootProject.name = "Cs"
includeBuild("AutoPlugin")
include(":app")
include(":cs")
include(":test1")
include(":test2")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {
            url = uri( "https://jitpack.io")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri( "https://jitpack.io")
        }
    }
}
