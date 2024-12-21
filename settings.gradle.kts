
pluginManagement {
    apply(from = "gradle/dependencies.gradle.kts")
    val settingsRepository : Action<RepositoryHandler> by extra
    repositories(settingsRepository)
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    apply(from = "gradle/dependencies.gradle.kts")
    val settingsRepository : Action<RepositoryHandler> by extra
    repositories(settingsRepository)
}


rootProject.name = "Cs"
includeBuild("AutoPlugin")

include(":app")
include(":cs")
//includeBuild("cs-annotation")
include(":test1")
include(":test2")
include(":AutoProcess")


