plugins {
    id("java")
    id("maven-publish")
}

publishing.repositories {
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

publishing {
    publications {
        create<MavenPublication>("annotation") {
            groupId = "com.brightk.cs"
            artifactId = "annotation"
            version = "0.1.0"
            from(components["java"])
        }
    }
}
