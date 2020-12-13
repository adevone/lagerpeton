plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdkVersion(targetVersion)

    defaultConfig {
        minSdkVersion(minVersion)
        targetSdkVersion(targetVersion)
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
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("io.adev:lagerpeton:$lagerpetonVersion")
    implementation("io.adev:lagerpeton-android:$lagerpetonVersion")

    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.core:core-ktx:1.3.2")

    implementation(kotlin("test"))
    implementation(kotlin("test-junit"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}