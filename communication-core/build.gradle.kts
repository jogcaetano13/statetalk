plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.dokka")
    `maven-publish`
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    api("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    api("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

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

group = Publish.GROUP_ID
version = Publish.LIBRARY_VERSION

publishing {
    publications {
        register<MavenPublication>("gprRelease") {
            groupId = Publish.GROUP_ID
            artifactId = Publish.ARTIFACT_CORE_ID
            version = Publish.LIBRARY_VERSION
            //artifact(mavenArtifactPath)
            from(components["java"])
        }
    }
}