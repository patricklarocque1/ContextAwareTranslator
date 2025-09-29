plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("org.jetbrains.kotlin.plugin.serialization")
}

android {
	namespace = "com.example.contextawaretranslator"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.example.contextawaretranslator"
		minSdk = 26
		targetSdk = 34
		versionCode = 1
		versionName = "0.1"
	}

	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.14"
	}
	packagingOptions {
		resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
	}
}

dependencies {
	implementation(project(":core-model"))
	implementation("androidx.core:core-ktx:1.13.1")
	implementation("androidx.activity:activity-compose:1.9.2")
	implementation("androidx.compose.ui:ui:1.7.2")
	implementation("androidx.compose.ui:ui-tooling-preview:1.7.2")
	debugImplementation("androidx.compose.ui:ui-tooling:1.7.2")
	implementation("androidx.compose.material3:material3:1.2.1")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
	implementation("androidx.work:work-runtime-ktx:2.9.1")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
}
