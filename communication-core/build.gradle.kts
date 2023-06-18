plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
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