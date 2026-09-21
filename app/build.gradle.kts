plugins {
    alias(libs.plugins.banumusagr.android.application)
    alias(libs.plugins.banumusagr.android.application.compose)
}

android {
    namespace = "com.example.banumusagr"

    defaultConfig {
        applicationId = "com.example.banumusagr"
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
    implementation(libs.androidx.activity.compose)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}