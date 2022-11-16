plugins {
    kotlin("multiplatform")
    id("convention.publication.multiplatform")
}

kotlin {
    jvm()
    ios {
        binaries {
            framework()
        }
    }
    iosArm32 {
        binaries {
            framework()
        }
    }
    iosSimulatorArm64 {
        binaries {
            framework()
        }
    }

    @Suppress("UNUSED_VARIABLE")
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
    }
}

group = lagerpetonGroup
version = lagerpetonVersion

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.6"
    }
}