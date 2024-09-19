package com.pokeskies.redeemed.storage

import com.pokeskies.redeemed.config.StorageSettings
import com.pokeskies.redeemed.data.CodeData
import com.pokeskies.redeemed.data.UserData
import com.pokeskies.redeemed.storage.database.MongoStorage
import com.pokeskies.redeemed.storage.database.sql.SQLStorage
import com.pokeskies.redeemed.storage.file.FileStorage
import java.util.*

interface IStorage {
    companion object {
        fun load(config: StorageSettings): IStorage {
            return when (config.type) {
                StorageType.JSON -> FileStorage()
                StorageType.MONGO -> MongoStorage(config)
                StorageType.MYSQL, StorageType.SQLITE -> SQLStorage(config)
            }
        }
    }

    fun getUser(uuid: UUID): UserData

    fun saveUser(uuid: UUID, userData: UserData): Boolean

    fun getCodeData(id: String): CodeData?

    fun saveCodeData(id: String, codeData: CodeData): Boolean

    fun close() {}
}
