dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "stateTalk"
include(":app")
include(":statetalk-android")
include(":statetalk-core")
include(":statetalk-paging")
