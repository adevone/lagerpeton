plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = targetVersion

    defaultConfig {
        minSdk = minVersion
        targetSdk = targetVersion
        applicationId = "io.adev.lagerpeton.example"
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            isDebuggable = true
        }
    }
    namespace = "io.adev.lagerpeton.example"
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("$lagerpetonGroup:lagerpeton:$lagerpetonVersion")
    implementation("$lagerpetonGroup:lagerpeton-android:$lagerpetonVersion")
//    implementation(project(":lagerpeton"))
//    implementation(project(":lagerpeton-android"))

    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.core:core-ktx:1.8.0")

    implementation(kotlin("test"))
    implementation(kotlin("test-junit"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}