rootProject.name = "Cs"
includeBuild("AutoPlugin")
include(":app")
include(":cs")
include(":test1")
include(":test2")

pluginManagement {
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
        gradlePluginPortal()
        google()

        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
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
        google()
        mavenCentral()

    }
}
include(":CsProcessor")
include(":CsAnnotation")
