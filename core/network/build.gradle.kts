import java.util.Properties

plugins {
    alias(libs.plugins.banumusa.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.aliayali.network"

    defaultConfig {
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")

        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { input ->
                localProperties.load(input)
            }
        }

        val token1 =
            localProperties.getProperty("NOTIFICATOR_TOKEN_1", "")

        val token2 =
            localProperties.getProperty("NOTIFICATOR_TOKEN_2", "")

        buildConfigField(
            "String",
            "NOTIFICATOR_TOKEN_1",
            "\"$token1\""
        )

        buildConfigField(
            "String",
            "NOTIFICATOR_TOKEN_2",
            "\"$token2\""
        )
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.core)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.kotlinx.serialization.json)
}