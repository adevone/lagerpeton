buildscript {
    repositories {
        jcenter()
        google()
        maven(url = "https://dl.bintray.com/summermpp/lagerpeton")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
    }
}