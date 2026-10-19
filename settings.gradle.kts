enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("build-logic")
    repositories {
        maven { url = uri("https://maven.myket.ir/") }
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://maven.myket.ir/") }
        google()
        mavenCentral()
    }
}

rootProject.name = "banumusagr"
include(":app")
include(":core:navigation")
include(":core:designsystem")
include(":feature:home")
include(":feature:report")
include(":core:database")
include(":core:data")
include(":core:domain")
include(":core:model")
