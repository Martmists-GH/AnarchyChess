import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("org.jetbrains.compose") version "1.2.1"
    id("com.android.application")
}

val development: String? by project
val isDevelopment = development != "false"

repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    js("web", IR) {
        browser {
            commonWebpackConfig {
                outputFileName = "app.js"
            }
        }
        binaries.executable()
    }

    jvm("desktop") {

    }

    android("app") {

    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.runtime)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("io.ktor:ktor-client-core:2.2.1")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.1")
                implementation("io.ktor:ktor-client-content-negotiation:2.2.1")
            }
        }

        val webMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(compose.web.core)
                implementation(compose.web.svg)

                implementation("io.ktor:ktor-client-js:2.2.1")
            }
        }

        val jvmMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("io.ktor:ktor-client-cio:2.2.1")
            }
        }

        val desktopMain by getting {
            dependsOn(jvmMain)

            dependencies {
                implementation(compose.desktop.common)
                implementation("ch.qos.logback:logback-classic:1.4.5")
            }
        }

        val appMain by getting {
            dependsOn(jvmMain)

            dependencies {
                // Avoid changing these as they change the minSdkVersion and become incompatible
                implementation("androidx.appcompat:appcompat:1.5.1")
                implementation("androidx.activity:activity-compose:1.5.0")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "dev.anarchy.app.MainKt"

        nativeDistributions {
            targetFormats(
                // MacOS
                TargetFormat.Dmg,
                // Windows
                TargetFormat.Msi,
                // Linux
                TargetFormat.Deb,
                TargetFormat.Rpm,
                TargetFormat.AppImage,
            )
        }
    }
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 26
        targetSdk = 32
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    sourceSets {
        named("main") {
            manifest.srcFile("src/appMain/AndroidManifest.xml")
            res.srcDirs("src/appMain/res")
        }
    }
}

tasks {
    named("build") {
        if (isDevelopment) {
            dependsOn(
                // Android
                "assembleDebug",
                // Desktop
                "packageDistributionForCurrentOS",
                // Web is handled by ac-backend
            )
        } else {
            dependsOn(
                // Android
                "assembleRelease",
                // Desktop
                "packageReleaseDistributionForCurrentOS",
                // Web is handled by ac-backend
            )
        }
    }
}
