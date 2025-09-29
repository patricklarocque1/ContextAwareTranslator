plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.example.core.translation"
    compileSdk = 34
    defaultConfig { minSdk = 26 }
}

dependencies {
    implementation("androidx.annotation:annotation:1.8.0")
}