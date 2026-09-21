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
        register("androidApplication") {
            id = "banumusagr.android.application"
            implementationClass =
                "com.banumusagr.convention.AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "banumusagr.android.library"
            implementationClass =
                "com.banumusagr.convention.AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "banumusagr.android.compose"
            implementationClass =
                "com.banumusagr.convention.AndroidComposeConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "banumusagr.android.library.compose"
            implementationClass =
                "com.banumusagr.convention.AndroidLibraryComposeConventionPlugin"
        }
    }
}