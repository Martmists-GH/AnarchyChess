package dev.anarchy.backend.site.plugins

import dev.anarchy.backend.BackendBuildConfig
import dev.anarchy.backend.config.ConfigLoader
import dev.anarchy.backend.database.DatabaseHandler
import dev.anarchy.backend.database.tables.MetadataTable
import dev.anarchy.backend.ext.hex
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import java.security.SecureRandom
import javax.crypto.KeyGenerator

data class GoogleOAuthCookie(
    val token: String,
    val expiresIn: Long,
    val refreshToken: String,
) : Principal {
    val expiresAt = System.currentTimeMillis() + expiresIn * 1000
}

data class OAuthRedirectCookie(
    val path: String,
)

fun Application.setupAuthentication() {
    val config = ConfigLoader.loadDefault()

    install(Authentication) {
        oauth("google-oauth") {
            val port = if (BackendBuildConfig.DEVELOPMENT) ":${config.site.port}" else ""

            urlProvider = {
                "${config.site.domain}$port/auth/callback"
            }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = config.oauth.clientId,
                    clientSecret = config.oauth.clientSecret,
                    defaultScopes = config.oauth.scopes,
                )
            }
        }

        session<GoogleOAuthCookie>("google-session") {
            challenge {
                call.sessions.set(OAuthRedirectCookie(call.request.path()))
                call.respondRedirect("/auth/login")
            }

            validate { session ->
                if (session.expiresAt > System.currentTimeMillis()) {
                    session
                } else {
                    null
                }
            }
        }
    }

    install(Sessions) {
        val encryptKeyName = "COOKIE_ENCRYPT_KEY"
        val signKeyName = "COOKIE_SIGN_KEY"

        val (encryptKey, signKey) = DatabaseHandler.transaction {
            val encryptKey = MetadataTable.getOrPut(encryptKeyName) {
                KeyGenerator.getInstance("HmacSHA256").apply {
                    init(512, SecureRandom())
                }.generateKey().encoded.hex()
            }

            val signKey = MetadataTable.getOrPut(signKeyName) {
                KeyGenerator.getInstance("AES").apply {
                    init(128, SecureRandom())
                }.generateKey().encoded.hex()
            }

            encryptKey to signKey
        }.get()

        cookie<GoogleOAuthCookie>("google_session_data") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60 * 60
            transform(SessionTransportTransformerEncrypt(hex(encryptKey), hex(signKey)))
        }

        cookie<OAuthRedirectCookie>("oauth_redirect") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 15 * 60
            transform(SessionTransportTransformerEncrypt(hex(encryptKey), hex(signKey)))
        }
    }
}
