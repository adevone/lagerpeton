import com.jfrog.bintray.gradle.BintrayExtension
import java.util.Properties

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven")
    id("maven-publish")
    id("com.jfrog.bintray")
}

android {
    compileSdkVersion(targetVersion)

    defaultConfig {
        minSdkVersion(1)
        targetSdkVersion(targetVersion)
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("io.adev:lagerpeton:$lagerpetonVersion")
//    implementation(project(":lagerpeton"))
}

val sourceJar by tasks.registering(Jar::class) {
    from(android.sourceSets.getByName("main").java.srcDirs)
    archiveClassifier.set("sources")
}

group = lagerpetonGroup
version = lagerpetonVersion

publishing {
    publications {
        create<MavenPublication>("lagerpetonAndroid") {
            groupId = project.group.toString()
            artifactId = project.name
            version = lagerpetonVersion
            artifact(tasks.getByName("sourceJar"))
            artifact("$buildDir/outputs/aar/${project.name}-release.aar")
        }
    }
}

val propsFile = File(rootProject.rootDir, "bintray.properties")
if (propsFile.exists()) {
    bintray {
        val bintrayProps = Properties().apply {
            load(propsFile.inputStream())
        }
        user = bintrayProps.getProperty("USERNAME")
        key = bintrayProps.getProperty("API_KEY")
        pkg(closureOf<BintrayExtension.PackageConfig> {
            repo = "summer"
            name = project.name
            userOrg = "summermpp"
            setLicenses("MIT")
            vcsUrl = "https://github.com/adevone/summer"
        })
        setPublications("lagerpetonAndroid")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.6"
    }
}