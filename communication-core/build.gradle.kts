plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.dokka")
    `maven-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // Gson
    api("com.google.code.gson:gson:2.10.1")
}

tasks {
    register("javadocJar", Jar::class) {
        dependsOn(named("dokkaHtml"))
        archiveClassifier.set("javadoc")
        from("$buildDir/dokka/html")
    }
}

publishing {

    repositories {
        maven {
            name = GitHub.NAME
            url = uri(GitHub.URL)
        }

        val mavenArtifactPath = "$buildDir/outputs/aar/${Publish.ARTIFACT_CORE_ID}-release.aar"

        publications {
            register<MavenPublication>("gprRelease") {
                groupId = Publish.GROUP_ID
                artifactId = Publish.ARTIFACT_CORE_ID
                version = Publish.LIBRARY_VERSION
                artifact(mavenArtifactPath)

                artifact(tasks.getByName("javadocJar"))

                pom {
                    withXml {
                        // add dependencies to pom
                        val dependencies = asNode().appendNode("dependencies")
                        configurations.api.get().dependencies.forEach {
                            if (it.group != null &&
                                "unspecified" != it.name &&
                                it.version != null) {

                                val dependencyNode = dependencies.appendNode("dependency")
                                dependencyNode.appendNode("groupId", it.group)
                                dependencyNode.appendNode("artifactId", it.name)
                                dependencyNode.appendNode("version", it.version)
                            }
                        }
                    }
                }
            }
        }
    }
}