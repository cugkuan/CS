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
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
include(":CsProcessor")
include(":CsAnnotation")
