plugins {
    alias(libs.plugins.banumusa.android.application)
    alias(libs.plugins.banumusa.android.application.compose)
}

android {
    namespace = "com.aliayali.navigation"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
}