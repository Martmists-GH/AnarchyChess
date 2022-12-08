package dev.anarchy.backend.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

object MetadataTable : Table() {
    val key = varchar("key", 255)
    val value = varchar("value", 255)

    override val primaryKey = PrimaryKey(key)

    context(Transaction) fun get(key: String): String {
        return select { MetadataTable.key eq key }.first()[value]
    }

    context(Transaction) fun getOrNull(key: String): String? {
        return select { MetadataTable.key eq key }.firstOrNull()?.get(value)
    }

    context(Transaction) fun getOrPut(key: String, default: () -> String): String {
        return getOrNull(key) ?: default().also { set(key, it) }
    }

    context(Transaction) fun set(key: String, value: String) {
        insert {
            it[MetadataTable.key] = key
            it[MetadataTable.value] = value
        }
    }
}
