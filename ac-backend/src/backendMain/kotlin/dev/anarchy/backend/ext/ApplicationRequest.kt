package dev.anarchy.backend.ext

import dev.anarchy.backend.site.templating.RequestMetadata
import io.ktor.server.plugins.*
import io.ktor.server.request.*

fun ApplicationRequest.metadata(): RequestMetadata {
    val baseUrl = "${origin.scheme}://${origin.serverHost}${if (origin.serverPort == 80 || origin.serverPort == 443) "" else ":" + origin.serverPort}"
    val path = path()
    return RequestMetadata(
        baseUrl = baseUrl,
        path = path,
        url = "$baseUrl$path",
    )
}
