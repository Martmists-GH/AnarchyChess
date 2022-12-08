package dev.anarchy.backend.ext

import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URL

inline fun <reified T : Any> T.getResourceAsStream(path: String): InputStream {
    val stream = Thread.currentThread().contextClassLoader.getResourceAsStream(path)
    return stream ?: T::class.java.getResourceAsStream(path) ?: throw FileNotFoundException("Resource not found: $path")
}

inline fun <reified T : Any> T.getResource(path: String): URL {
    val url = Thread.currentThread().contextClassLoader.getResource(path)
    return url ?: T::class.java.getResource(path) ?: throw FileNotFoundException("Resource not found: $path")
}
