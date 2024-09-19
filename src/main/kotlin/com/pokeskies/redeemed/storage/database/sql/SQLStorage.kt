package com.pokeskies.redeemed.storage.database.sql

import com.pokeskies.redeemed.Redeemed
import com.pokeskies.redeemed.config.StorageSettings
import com.pokeskies.redeemed.data.CodeData
import com.pokeskies.redeemed.data.UserData
import com.pokeskies.redeemed.storage.IStorage
import com.pokeskies.redeemed.storage.StorageType
import com.pokeskies.redeemed.storage.database.sql.providers.MySQLProvider
import com.pokeskies.redeemed.storage.database.sql.providers.SQLiteProvider
import java.sql.SQLException
import java.util.*

class SQLStorage(config: StorageSettings) : IStorage {
    private val connectionProvider: ConnectionProvider = when (config.type) {
        StorageType.MYSQL -> MySQLProvider(config)
        StorageType.SQLITE -> SQLiteProvider(config)
        else -> throw IllegalStateException("Invalid storage type!")
    }

    init {
        connectionProvider.init()
    }

    override fun getUser(uuid: UUID): UserData {
        var usedCodes: MutableList<String> = mutableListOf()
        try {
            connectionProvider.createConnection().use {
                val statement = it.createStatement()
                val result = statement.executeQuery(String.format("SELECT * FROM userData WHERE uuid='%s'", uuid.toString()))
                if (result != null && result.next()) {
                    usedCodes = Redeemed.INSTANCE.gson.fromJson(result.getString("usedCodes"), UserData.LIST_TYPE)
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return UserData(usedCodes)
    }

    override fun saveUser(uuid: UUID, userData: UserData): Boolean {
        return try {
            connectionProvider.createConnection().use {
                val statement = it.createStatement()
                statement.execute(
                    String.format("REPLACE INTO userData (uuid, usedCodes) VALUES ('%s', '%s')",
                        uuid.toString(),
                        Redeemed.INSTANCE.gson.toJson(userData.usedCodes)
                    )
                )
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun getCodeData(id: String): CodeData? {
        try {
            connectionProvider.createConnection().use {
                val statement = it.createStatement()
                val result = statement.executeQuery(String.format("SELECT * FROM codeData WHERE id='%s'", id))
                if (result != null && result.next()) {
                    return CodeData(result.getInt("uses"))
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    override fun saveCodeData(id: String, codeData: CodeData): Boolean {
        return try {
            connectionProvider.createConnection().use {
                val statement = it.createStatement()
                statement.execute(
                    String.format("REPLACE INTO codeData (id, uses) VALUES ('%s', '%d')",
                        id,
                        codeData.uses
                    )
                )
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun close() {
        connectionProvider.shutdown()
    }
}
