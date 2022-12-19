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

val build_version = getGitTag() ?: "v1.0.0-${getGitSha()}"
version = build_version

val localProperties = File("local.properties")
if (!localProperties.exists()) {
    localProperties.createNewFile()
}
Properties().also { it.load(localProperties.reader()) }.forEach { (k, v) ->
    project.extra.set(k.toString(), v)
}

val development by transformedProperty { it != "false" }

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
        packageName("$group.${project.name.removePrefix("${rootProject.name}-").replace('-', '_')}")
        buildConfigField("String", "VERSION", "\"$version\"")
        buildConfigField("String", "GROUP", "\"$group\"")
        buildConfigField("Boolean", "DEVELOPMENT", "$development")
        buildConfigField("String", "COMMIT_COUNT", "\"${getCommitCount()}\"")
        buildConfigField("String", "COMMIT_SHA", "\"${getGitSha()}\"")
        buildConfigField("String", "BUILD_TIME", "\"${getBuildTime()}\"")
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_11.toString()
                if (!development) {
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
                if (!development) {
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
