plugins {
    id("com.android.library")
    kotlin("android")
    id("org.jetbrains.dokka")
    `maven-publish`
}

val libVersion = "1.0.10"

android {
    compileSdk = Config.COMPILE_SDK

    defaultConfig {
        minSdk = Config.MIN_SDK
        targetSdk = Config.TARGET_SDK
        version = libVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

tasks {
    register("javadocJar", Jar::class) {
        dependsOn(named("dokkaHtml"))
        archiveClassifier.set("javadoc")
        from("$buildDir/dokka/html")
    }

    register("sourcesJar", Jar::class) {
        archiveClassifier.set("sources")
        from(android.sourceSets.getByName("main").java.srcDirs)
    }
}

publishing {

    repositories {
        maven {
            name = GitHub.NAME
            url = uri(GitHub.URL)
        }

        val mavenArtifactPath = "$buildDir/outputs/aar/${Publish.ARTIFACT_ID}-release.aar"

        publications {
            register<MavenPublication>("gprRelease") {
                groupId = Publish.GROUP_ID
                artifactId = Publish.ARTIFACT_ID
                version = libVersion
                artifact(mavenArtifactPath)

                artifact(tasks.getByName("javadocJar"))
                artifact(tasks.getByName("sourcesJar"))

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

dependencies {

    implementation("androidx.core:core-ktx:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // Gson
    api("com.google.code.gson:gson:2.9.0")

    // Live data
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")

    // Paging
    api("androidx.paging:paging-runtime-ktx:3.1.1")
    api("androidx.paging:paging-common-ktx:3.1.1")
    api("androidx.room:room-paging:2.4.3")

    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:5.0.0-alpha.2")
    testImplementation("org.json:json:20200518")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
}