buildscript {
    repositories {
        mavenCentral()  // required for some dependencies of the gradle module
        maven("https://maven.martmists.com/releases")
    }
    dependencies {
        classpath("com.martmists.commons:commons-gradle:1.0.1")
    }
}

rootProject.name = "ac"

include(
    ":ac-common",
    ":ac-frontend",
    ":ac-backend",
)
