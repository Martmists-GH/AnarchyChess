import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("org.jetbrains.compose") version "1.2.1"
    id("com.android.application")
}

val development by transformedProperty { it != "false" }

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
                implementation(project(":ac-common"))

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
                implementation(compose.desktop.currentOs)
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
            packageName = "AnarchyChess"
            description = "Chess but 2"
            version = (project.version as String).substringAfter('v').substringBefore('-')
            copyright = "© 2022 Anarchy Chess Developers. All rights reserved."
            licenseFile.set(rootDir.resolve("LICENSE"))

            outputBaseDir.set(rootProject.buildDir.resolve("dist"))
            modules("java.instrument", "jdk.unsupported")

            targetFormats(
                // MacOS
                TargetFormat.Dmg,
                // Windows
                TargetFormat.Msi,
                // Linux
                TargetFormat.Deb,
            )

            windows {
                perUserInstall = true
                dirChooser = true
            }
            linux {

            }
            macOS {
                bundleID = group as String
            }
        }

        buildTypes.release.proguard {
            configurationFiles.from(projectDir.resolve("proguard-desktop-rules.pro"))
        }
    }
}

compose.experimental {
    web.application { }
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 26
        targetSdk = 32
        versionName = project.version as String
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildTypes {
        debug {
            versionNameSuffix = "-debug"
            proguardFiles(getDefaultProguardFile("proguard-defaults.txt"), projectDir.resolve("proguard-android-rules.pro"))
        }
        release {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), projectDir.resolve("proguard-android-rules.pro"))
        }
    }

    sourceSets {
        named("main") {
            manifest.srcFile("src/appMain/AndroidManifest.xml")
            res.srcDirs("src/appMain/res")
        }
    }
}

tasks {
    named("preBuild") {
        dependsOn("clean")
    }

    named("build") {
        if (development) {
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
