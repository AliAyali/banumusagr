
plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "com.banumusagr.buildlogic"

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidLibraryDesugaring") {
            id = "com.banumusa.android.library.desugaring"
            implementationClass =
                "com.banumusagr.convention.AndroidLibraryDesugaringConventionPlugin"
        }

        register("androidApplication") {
            id = "com.banumusa.android.application"
            implementationClass =
                "com.banumusagr.convention.AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "com.banumusa.android.library"
            implementationClass =
                "com.banumusagr.convention.AndroidLibraryConventionPlugin"
        }

        register("androidCompose") {
            id = "com.banumusa.android.compose"
            implementationClass =
                "com.banumusagr.convention.AndroidComposeConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "com.banumusa.android.library.compose"
            implementationClass =
                "com.banumusagr.convention.AndroidLibraryComposeConventionPlugin"
        }
    }
}