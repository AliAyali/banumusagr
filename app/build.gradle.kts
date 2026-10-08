plugins {
    alias(libs.plugins.banumusagr.android.application)
    alias(libs.plugins.banumusagr.android.application.compose)
}

android {
    namespace = "com.aliayali.banumusagr"

    defaultConfig {
        applicationId = "com.aliayali.banumusagr"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.material.icons.extended)
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}