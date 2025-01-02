import java.util.Properties

val file = rootProject.file("../local.properties")
val properties = Properties().apply {
    load(file.inputStream())
}
val testUrl :String by  properties
val testName :String  by  properties
val testPassword :String by  properties
val ossrhUsername :String by  properties
val ossrhPassword :String  by  properties

val uploadRepository: Action<RepositoryHandler> = Action<RepositoryHandler> {
    maven {
        val publicUrl = when {
            version.toString()
                .endsWith("SNAPSHOT") -> "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            version.toString().endsWith(".test") -> testUrl
            else -> "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
        }
        url = uri(publicUrl)
        isAllowInsecureProtocol = true
        credentials {
            if (version.toString().endsWith(".test")) {
                username = testName
                password = testPassword
            } else {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
    mavenLocal()
}
mapOf(
    "uploadRepository" to uploadRepository
).forEach { (name, closure) ->
    project.extra.set(name, closure)
}