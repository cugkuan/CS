pluginManagement {
    apply(from = "../gradle/dependencies.gradle.kts")
    val settingsRepository : Action<RepositoryHandler> by extra
    repositories(settingsRepository)
}

dependencyResolutionManagement {
    apply(from = "../gradle/dependencies.gradle.kts")
    val settingsRepository : Action<RepositoryHandler> by extra
    @Suppress("UnstableApiUsage")
    repositories(settingsRepository)
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}