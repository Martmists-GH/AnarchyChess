package dev.anarchy.backend.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.io.InputStream

object ConfigLoader {
    private val mapper = ObjectMapper(YAMLFactory()).also {
        it.registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()
        )
    }

    fun loadDefault() = load<RootConfig>("site-config.yaml", "config/config.yaml")

    inline fun <reified T> load(path: String, default: String) = load<T>(File(path), default)
    inline fun <reified T> load(file: File, default: String) = load(file, T::class.java, T::class.java.getResourceAsStream("/$default")!!)
    fun <T> load(file: File, type: Class<T>, default: InputStream): T {
        if (!file.exists()) {
            file.createNewFile()
            file.outputStream().use { out ->
                default.copyTo(out)
            }
        }
        return mapper.readValue(file, type) as T
    }
}
