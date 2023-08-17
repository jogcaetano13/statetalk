plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
    implementation("com.android.tools.build:gradle:8.1.0")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.5.0")
    implementation("org.jetbrains.dokka:dokka-core:1.5.0")
}