import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.miret.etienne.gradle.sass.CompileSass
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.miret.etienne.sass") version "1.4.1"
    id("com.github.gmazzo.buildconfig")
}

val development: String? by project
val isDevelopment = development != "false"

kotlin {
    jvm("backend") {
        withJava()
    }

    js("frontend", IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled = true
                }
                outputFileName = "index.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":ac-common"))
            }
        }

        val backendMain by getting {
            dependencies {
                // Kotlinx libraries
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

                // Ktor server
                for (module in listOf(
                    // Core
                    "core", "tomcat",

                    // Authentication for adding new content eventually
                    "auth", "sessions",

                    // Plugins: Headers
                    "auto-head-response", "default-headers", "compression", "forwarded-header",

                    // Plugins: Content
                    "content-negotiation", "status-pages",

                    // Plugins: Misc
                    "call-logging",
                )) {
                    implementation("io.ktor:ktor-server-$module-jvm:2.2.1")
                }

                // Database libraries
                for (module in listOf(
                    "core", "jdbc", "java-time"
                )) {
                    implementation("org.jetbrains.exposed:exposed-$module:0.41.1")
                }

                // Config
                implementation("org.yaml:snakeyaml:1.33")
                implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.1")

                // Database drivers
                if (isDevelopment) {
                    implementation("com.h2database:h2:2.1.214")
                    implementation("org.xerial:sqlite-jdbc:3.40.0.0")
                }
                implementation("org.postgresql:postgresql:42.5.1")

                // Logging
                implementation("ch.qos.logback:logback-classic:1.4.5")

                // Utilities
                implementation("com.martmists.commons:commons-jvm:1.0.4")
            }
        }
    }
}

application {
    mainClass.set("dev.anarchy.backend.MainKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

sass {
    version = "1.54.1"
    directory = file ("${rootDir.absolutePath}/.gradle/sass")
}

tasks {
    val compileSass by named<CompileSass>("compileSass") {
        outputDir = file("${buildDir}/sass")
        setSourceDir(file("${projectDir}/src/frontendMain/sass"))
        loadPath(file("sass-lib"))
        style = compressed
        sourceMap = file
        sourceMapUrls = relative
    }

    named<Copy>("backendProcessResources") {
        outputs.upToDateWhen { false }  // Bug in Webpack Task

        val frontendTask = getByName("frontendBrowser${if (isDevelopment) "Development" else "Production"}Webpack")
        val appTask = project(":ac-frontend").tasks.getByName("webBrowser${if (isDevelopment) "Development" else "Production"}Webpack")

        into("/static/js") {
            from(frontendTask) {
                include("index.js")
                if (isDevelopment) {
                    include("index.js.map")
                }
            }
            from(appTask) {
                include("app.js")
                if (isDevelopment) {
                    include("app.js.map")
                }
            }
        }
        into("/static/css") {
            from(compileSass)
        }
    }

    val shadowJar by named<ShadowJar>("shadowJar") {
        manifest {
            attributes(mapOf(
                "Main-Class" to "dev.anarchy.backend.MainKt"
            ))
        }
    }

    named("build") {
        dependsOn(shadowJar)
    }

    named<JavaExec>("run") {
        workingDir(rootDir.resolve("run").also { if (!it.exists()) it.mkdirs() })
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += listOf(
                "-Xcontext-receivers"
            )
        }
    }
}
