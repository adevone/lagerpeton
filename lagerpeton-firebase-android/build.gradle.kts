plugins {
    id("com.android.library")
    id("kotlin-android")
    id("convention.publication.android")
}

android {
    compileSdk = targetVersion

    defaultConfig {
        minSdk = 16
        targetSdk = targetVersion
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    namespace = "lagerpeton.firebase.android"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    implementation(platform("com.google.firebase:firebase-bom:29.0.3"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    implementation(project(":lagerpeton"))
}

group = lagerpetonGroup
version = lagerpetonVersion

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.6"
    }
}