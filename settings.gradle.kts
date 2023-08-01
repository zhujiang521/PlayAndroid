pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven("https://jitpack.io")
        maven("https://s01.oss.sonatype.org/content/groups/public")

        mavenCentral()
    }
}
rootProject.name = "PlayAndroid"
include(":app")
include(":model")
include(":network")
include(":core")