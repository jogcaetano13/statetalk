plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
}

object Plugins {
    const val AGP = "4.1.3"
    const val DOKKA = "1.5.0"
    const val KOTLIN = "1.7.10"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Plugins.KOTLIN}")
    implementation("com.android.tools.build:gradle:7.2.2")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:${Plugins.DOKKA}")
    implementation("org.jetbrains.dokka:dokka-core:${Plugins.DOKKA}")
}