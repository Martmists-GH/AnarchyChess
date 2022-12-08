package dev.anarchy.backend.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable() {
    val email = varchar("email", 255).uniqueIndex()
    val username = varchar("username", 255).uniqueIndex()
    val accessToken = varchar("access_token", 2048)
    val refreshToken = varchar("refresh_token", 512)
    val needsReauth = bool("needs_reauth")
    val token = varchar("token", 512)
}
