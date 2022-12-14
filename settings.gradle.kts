pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

buildscript {
    repositories {
        mavenCentral()  // required for some dependencies of the gradle module
        maven("https://maven.martmists.com/releases")
        google()
    }
    dependencies {
        classpath("com.martmists.commons:commons-gradle:1.0.4")
        classpath("com.android.tools.build:gradle:7.0.4")
    }
}

rootProject.name = "ac"

include(
    ":ac-common",
    ":ac-frontend",
    ":ac-backend",
)
