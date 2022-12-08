package dev.anarchy.backend.ext

import com.martmists.commons.ktor.ext.locale
import dev.anarchy.backend.config.ConfigLoader
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.pebble.*
import io.ktor.server.response.*

suspend fun ApplicationCall.respondTemplate(status: HttpStatusCode, template: String, properties: Map<String, Any> = emptyMap()) {
    val props = mapOf(
        "request" to request.metadata(),
        "config" to ConfigLoader.loadDefault(),
    )
    respond(status, PebbleContent(template, props + properties, locale = request.locale()))
}
