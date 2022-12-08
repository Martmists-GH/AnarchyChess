package dev.anarchy.backend.config

import com.fasterxml.jackson.annotation.JsonProperty

data class OAuthConfig(
    @JsonProperty("client_id")
    val clientId: String,
    @JsonProperty("client_secret")
    val clientSecret: String,
    val scopes: List<String>,
)
