package com.pokeskies.redeemed.storage.database.sql.providers

import com.pokeskies.redeemed.config.StorageSettings
import com.zaxxer.hikari.HikariConfig

class MySQLProvider(val config: StorageSettings) : HikariCPProvider(config) {
    override fun getConnectionURL(): String = String.format(
        "jdbc:mysql://%s:%d/%s",
        config.host,
        config.port,
        config.database
    )

    override fun getDriverClassName(): String = "com.mysql.cj.jdbc.Driver"
    override fun getDriverName(): String = "mysql"
    override fun configure(config: HikariConfig) {}
}
