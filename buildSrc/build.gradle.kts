plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0")
    implementation("com.android.tools.build:gradle:8.10.0")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:2.0.0")
    implementation("org.jetbrains.dokka:dokka-core:2.0.0")
}