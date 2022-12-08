package dev.anarchy.backend.database

import com.martmists.commons.database.ThreadedDatabase
import dev.anarchy.backend.config.ConfigLoader
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

object DatabaseHandler {
    private val handler = ThreadedDatabase(createDb = DatabaseHandler::loadDatabase)

    fun initialize() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(

            )
        }.get()
    }

    fun <T> transaction(block: Transaction.() -> T) = handler.transaction(block)
    suspend fun <T> transactionAsync(block: Transaction.() -> T) = handler.transaction {
        runBlocking {
            block()
        }
    }.await()

    private fun loadDatabase(): Database {
        val config = ConfigLoader.loadDefault()
        val dbConf = config.database

        return when (dbConf.driver) {
            DatabaseDriver.SQLITE -> {
                TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
                Database.connect("jdbc:sqlite:./${dbConf.database}.db", "org.sqlite.JDBC")
            }

            DatabaseDriver.H2 -> {
                Database.connect("jdbc:h2:./${dbConf.database}", "org.h2.Driver")
            }

            DatabaseDriver.POSTGRESQL -> {
                Database.connect("jdbc:postgresql://${dbConf.host}:${dbConf.port}/${dbConf.database}", "org.postgresql.Driver", dbConf.username, dbConf.password)
            }
        }
    }
}
