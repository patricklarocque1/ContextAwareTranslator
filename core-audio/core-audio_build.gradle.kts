plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.example.core.audio"
    compileSdk = 34
    defaultConfig { minSdk = 26 }
}

dependencies {
    implementation("androidx.annotation:annotation:1.8.0")
}