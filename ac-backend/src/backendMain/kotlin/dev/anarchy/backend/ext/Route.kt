package dev.anarchy.backend.ext

import com.martmists.commons.ktor.ext.respondTemplate
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.getTemplate(path: String) {
    get("/$path") {
        call.respondTemplate("pages/$path", emptyMap())
    }
}
