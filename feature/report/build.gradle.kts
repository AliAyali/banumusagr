plugins {
    alias(libs.plugins.banumusa.android.application)
    alias(libs.plugins.banumusa.android.application.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.aliayali.report"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(projects.feature.report)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(projects.core.navigation)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material.icons.extended)
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}