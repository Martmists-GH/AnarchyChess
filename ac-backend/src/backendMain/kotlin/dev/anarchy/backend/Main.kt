@file:Suppress("ExtractKtorModule")

package dev.anarchy.backend

import dev.anarchy.backend.ext.getTemplate
import dev.anarchy.backend.site.plugins.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.pebble.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.tomcat.*
import org.slf4j.event.Level

fun main() {
    embeddedServer(Tomcat, port = 8182, host = "0.0.0.0") {
        install(CallLogging) {
            level = Level.INFO
        }
        setupHeaders()
        setupContent()
        setupAuthentication()

        // TODO: Routing
        routing {
            static("/static") {
                resources("/static")
            }

            get("/") {
                call.respondTemplate("pages/index", emptyMap())
            }

            route("/auth") {
                authenticate("google-oauth") {
                    get("/login") {
                        // Handled internally
                    }

                    get("/callback") {
                        val principal = call.principal<OAuthAccessTokenResponse.OAuth2>()
                        call.sessions.set(GoogleOAuthCookie(principal!!.accessToken, principal.expiresIn, principal.refreshToken!!))
                        val redirect = call.sessions.get<OAuthRedirectCookie>()
                        call.sessions.clear<OAuthRedirectCookie>()
                        call.respondRedirect(redirect?.path ?: "/")
                    }
                }

                get("/logout") {
                    call.sessions.clear<GoogleOAuthCookie>()
                    call.respondRedirect("/")
                }
            }
        }
    }.start(wait = true)
}
