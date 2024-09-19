package com.pokeskies.redeemed.data

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class UserData(
    val usedCodes: MutableList<String> = mutableListOf()
) {
    companion object {
        val LIST_TYPE: Type = object : TypeToken<MutableList<String>>() {}.type
    }

    override fun toString(): String {
        return "UserData(codes=$usedCodes)"
    }
}
