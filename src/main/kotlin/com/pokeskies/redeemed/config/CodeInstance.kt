package com.pokeskies.redeemed.config

import com.google.gson.annotations.SerializedName

class CodeInstance(
    @SerializedName("max_uses", alternate = ["maxUses"])
    val maxUses: Int = 0,
    val commands: List<String> = emptyList()
) {
    fun getMaxUses(): String {
        return if (maxUses <= 0) "∞" else maxUses.toString()
    }

    override fun toString(): String {
        return "Code(maxUses=$maxUses, commands=$commands)"
    }
}
