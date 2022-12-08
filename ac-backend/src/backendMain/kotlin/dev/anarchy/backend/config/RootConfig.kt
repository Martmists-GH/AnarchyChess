package dev.anarchy.backend.config

data class RootConfig(
    val site: SiteConfig,
    val database: DatabaseConfig,
    val oauth: OAuthConfig,
)
