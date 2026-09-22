plugins {
    alias(libs.plugins.banumusagr.android.library)
    alias(libs.plugins.banumusagr.android.library.compose)
}

android {
    namespace = "com.aliayali.designsystem"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
}