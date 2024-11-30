import java.util.Properties

val file = File("./local.properties")
val properties = Properties().apply {
    load(file.inputStream())
}
val testUrl :String by  properties
val testName :String  by  properties
val testPassword :String by  properties

private val repository: Action<RepositoryHandler> = Action<RepositoryHandler> {
    mavenLocal()
    maven {
        url = uri(testUrl)
        credentials {
            username = testName
            password = testPassword
        }
        isAllowInsecureProtocol = true
    }
    gradlePluginPortal()
    google()
    mavenCentral()
    maven {
        url = uri( "https://jitpack.io")
    }
}
mapOf(
    "settingsRepository" to repository
).forEach { (name, closure) ->
    settings.extra.set(name, closure)
}
