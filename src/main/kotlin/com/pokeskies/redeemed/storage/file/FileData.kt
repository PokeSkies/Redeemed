package com.pokeskies.redeemed.storage.file

import com.pokeskies.redeemed.data.CodeData
import com.pokeskies.redeemed.data.UserData
import java.util.*

class FileData {
    var codeData: HashMap<String, CodeData> = HashMap()
    var userData: HashMap<UUID, UserData> = HashMap()

    override fun toString(): String {
        return "FileData(codeData=$codeData, userData=$userData)"
    }
}
