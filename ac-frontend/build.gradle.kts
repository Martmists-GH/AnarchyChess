import io.miret.etienne.gradle.sass.CompileSass

plugins {
    id("org.jetbrains.compose") version "1.2.1"
    id("io.miret.etienne.sass") version "1.4.1"
}

kotlin {
    js("app", IR) {
        browser {
            commonWebpackConfig {
                outputFileName = "app.js"
            }
        }
        binaries.executable()
    }
    sourceSets {
        val appMain by getting {
            dependencies {
                implementation(project(":ac-common"))

                implementation(compose.web.core)
                implementation(compose.web.svg)
                implementation(compose.runtime)

                // KTor - Client
                implementation("io.ktor:ktor-client-core:2.2.1")
                implementation("io.ktor:ktor-client-js:2.2.1")
                implementation("io.ktor:ktor-client-content-negotiation-js:2.2.1")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.1")
            }
        }
    }
}

sass {
    version = "1.54.1"
    directory = file ("${rootDir.absolutePath}/.gradle/sass")
}

tasks {
    val compileSass by named<CompileSass>("compileSass") {
        outputDir = file("${buildDir}/sass")
        setSourceDir(file("${projectDir}/src/appMain/sass"))
        loadPath(file("sass-lib"))
        style = compressed
        sourceMap = file
        sourceMapUrls = relative
    }
}
