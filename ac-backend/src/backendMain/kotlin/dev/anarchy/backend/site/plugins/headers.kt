package dev.anarchy.backend.site.plugins

import dev.anarchy.backend.BackendBuildConfig
import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.forwardedheaders.*

fun Application.setupHeaders() {
    if (!BackendBuildConfig.DEVELOPMENT) {
        install(Compression) {
            gzip {
                priority = 1.0
            }
            deflate {
                priority = 10.0
                minimumSize(1024)
            }
        }
    }
    install(DefaultHeaders) {
        header("X-Engine", "Ktor")
        header("X-Powered-By", "r/AnarchyChess")
    }
    install(XForwardedHeaders)
}
