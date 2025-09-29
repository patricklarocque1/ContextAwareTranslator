import org.gradle.api.JavaVersion

plugins {
	id("com.android.application") version "8.5.2" apply false
	id("com.android.library") version "8.5.2" apply false
	id("org.jetbrains.kotlin.android") version "1.9.24" apply false
	id("org.jetbrains.kotlin.plugin.serialization") version "1.9.24" apply false
}

allprojects {
	repositories {
		google()
		mavenCentral()
	}
}

subprojects {
	afterEvaluate {
		// Common Kotlin options if android module
	}
}
