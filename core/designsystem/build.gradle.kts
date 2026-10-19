plugins {
    alias(libs.plugins.banumusa.android.application)
    alias(libs.plugins.banumusa.android.application.compose)
}

android {
    namespace = "com.aliayali.designsystem"
}

dependencies {
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
}