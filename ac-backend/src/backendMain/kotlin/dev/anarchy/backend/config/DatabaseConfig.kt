package dev.anarchy.backend.config

import dev.anarchy.backend.database.DatabaseDriver

data class DatabaseConfig(
    val driver: DatabaseDriver,
    val host: String,
    val port: Int,
    val database: String,
    val username: String,
    val password: String,
)
