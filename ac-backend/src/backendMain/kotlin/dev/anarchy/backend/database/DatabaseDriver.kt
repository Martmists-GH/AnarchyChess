package dev.anarchy.backend.database

import com.fasterxml.jackson.annotation.JsonProperty

enum class DatabaseDriver {
    @JsonProperty("sqlite")
    SQLITE,
    @JsonProperty("h2")
    H2,
    @JsonProperty("postgres")
    POSTGRESQL,
}
