pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://example.com/maven-repo")
        }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "NeWork"
include(":app")