package dev.anarchy.backend.site.plugins

import com.martmists.commons.ktor.PebblePatch
import com.mitchellbosecke.pebble.loader.ClasspathLoader
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.pebble.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.statuspages.*
import dev.anarchy.ac_backend.BackendBuildConfig
import dev.anarchy.backend.ext.respondTemplate

fun Application.setupContent() {
    install(AutoHeadResponse)

    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, _ ->
            call.respondTemplate(HttpStatusCode.NotFound, "pages/error/404")
        }

        exception<Exception> { call, err ->
            call.respondTemplate(HttpStatusCode.InternalServerError, "pages/error/500", mapOf(
                "error" to err.stackTraceToString()
            ))
        }
    }

    install(if (BackendBuildConfig.DEVELOPMENT) PebblePatch else Pebble) {
        loader(ClasspathLoader().apply {
            // Weird thing in java with resources and classpaths
            // In a jar release they need to start with / to be found
            // In IDE they should be relative to the resource root
            prefix = if (BackendBuildConfig.DEVELOPMENT) "templates/" else "/templates/"
            suffix = ".peb"
        })
        cacheActive(!BackendBuildConfig.DEVELOPMENT)
    }
}
