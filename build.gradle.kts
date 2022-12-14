import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.gmazzo.gradle.plugins.generators.BuildConfigKotlinGenerator
import com.martmists.commons.*
import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*


plugins {
    kotlin("multiplatform") version "1.7.20" apply false
    kotlin("plugin.serialization") version "1.7.20" apply false
    id("com.github.ben-manes.versions") version "0.44.0"
    id("com.github.gmazzo.buildconfig") version "3.0.3"
}

group = "dev.anarchy"
version = "1.0.0"

val localProperties = File("local.properties")
if (!localProperties.exists()) {
    localProperties.createNewFile()
}
Properties().also { it.load(localProperties.reader()) }.forEach { (k, v) ->
    project.extra.set(k.toString(), v)
}

val development: String? by project
val isDevelopment = development != "false"

subprojects {
    apply(plugin="org.jetbrains.kotlin.multiplatform")
    apply(plugin="org.jetbrains.kotlin.plugin.serialization")
    apply(plugin="com.github.gmazzo.buildconfig")

    group = rootProject.group
    version = rootProject.version
    buildDir = rootProject.buildDir.resolve(name)

    repositories {
        martmists()
        mavenCentral()
        google()
    }

    buildConfig {
        generator(BuildConfigKotlinGenerator())
        className("${project.name.removePrefix("${rootProject.name}-").capitalized()}BuildConfig")
        packageName("$group.${project.name.replace('-', '_')}")
        buildConfigField("String", "VERSION", "\"$version\"")
        buildConfigField("String", "GROUP", "\"$group\"")
        buildConfigField("Boolean", "DEVELOPMENT", "$isDevelopment")
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_11.toString()
                if (!isDevelopment) {
                    freeCompilerArgs += listOf(
                        "-Xno-call-assertions",
                        "-Xno-param-assertions",
                        "-Xno-receiver-assertions",
                    )
                }
            }
        }

        withType<Kotlin2JsCompile> {
            kotlinOptions {
                if (!isDevelopment) {
                    freeCompilerArgs += listOf(
                        "-Xir-minimized-member-names",
                        "-source-map-embed-sources=never",
                    )
                } else {
                    freeCompilerArgs += listOf(
                        "-source-map-embed-sources=always",
                    )
                }
            }
        }

        withType<DependencyUpdatesTask> {
            rejectVersionIf {
                isStable(currentVersion) && !isStable(candidate.version)
            }
        }

        rootProject.tasks.named("prepareKotlinBuildScriptModel") {
            dependsOn(named("generateBuildConfig"))
        }
    }
}
