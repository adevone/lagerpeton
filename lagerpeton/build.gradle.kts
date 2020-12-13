plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    google()
}

android {
    compileSdkVersion(29)

    defaultConfig {
        minSdkVersion(1)
        targetSdkVersion(29)
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_6
        sourceCompatibility = JavaVersion.VERSION_1_6
    }
}

kotlin {
    jvm()
    android()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
    }
}