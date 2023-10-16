dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Communication"
include(":app")
include(":communication-android")
include(":communication-core")
include(":communication-paging")
