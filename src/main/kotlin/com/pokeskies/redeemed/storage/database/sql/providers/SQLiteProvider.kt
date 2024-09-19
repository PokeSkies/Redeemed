package com.pokeskies.redeemed.storage.database.sql.providers

import com.pokeskies.redeemed.Redeemed
import com.pokeskies.redeemed.config.StorageSettings
import com.zaxxer.hikari.HikariConfig
import java.io.File

class SQLiteProvider(config: StorageSettings) : HikariCPProvider(config) {
    override fun getConnectionURL(): String = String.format(
        "jdbc:sqlite:%s",
        File(Redeemed.INSTANCE.configDir, "storage.db").toPath().toAbsolutePath()
    )

    override fun getDriverClassName(): String = "org.sqlite.JDBC"
    override fun getDriverName(): String = "sqlite"
    override fun configure(config: HikariConfig) {}
}
