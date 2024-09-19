package com.pokeskies.redeemed.storage.file

import com.pokeskies.redeemed.config.ConfigManager
import com.pokeskies.redeemed.data.CodeData
import com.pokeskies.redeemed.data.UserData
import com.pokeskies.redeemed.storage.IStorage
import java.util.*

class FileStorage : IStorage {
    private var fileData: FileData = ConfigManager.loadFile(STORAGE_FILENAME, FileData(), true)

    companion object {
        private const val STORAGE_FILENAME = "storage.json"
    }

    override fun getUser(uuid: UUID): UserData {
        val userData = fileData.userData[uuid]
        return userData ?: UserData()
    }

    override fun saveUser(uuid: UUID, userData: UserData): Boolean {
        fileData.userData[uuid] = userData
        return ConfigManager.saveFile(STORAGE_FILENAME, fileData)
    }

    override fun getCodeData(id: String): CodeData? {
        return fileData.codeData[id]
    }

    override fun saveCodeData(id: String, codeData: CodeData): Boolean {
        fileData.codeData[id] = codeData
        return ConfigManager.saveFile(STORAGE_FILENAME, fileData)
    }
}
