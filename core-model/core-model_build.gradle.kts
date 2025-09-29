plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	id("org.jetbrains.kotlin.plugin.serialization")
}

android {
	namespace = "com.example.core.model"
	compileSdk = 34
	defaultConfig { minSdk = 26 }
	buildFeatures { buildConfig = true }
}

dependencies {
	implementation("androidx.core:core-ktx:1.13.1")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}
