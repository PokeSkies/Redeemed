package com.pokeskies.redeemed.config

import com.google.gson.annotations.SerializedName
import com.pokeskies.redeemed.utils.TextUtils
import net.kyori.adventure.audience.Audience

class MessageSettings(
    @SerializedName("invalid_code", alternate = ["invalidCode"])
    val invalidCode: String = "<red>The code '<dark_red>{0}</dark_red>' is invalid!",
    @SerializedName("already_redeemed", alternate = ["alreadyRedeemed"])
    val alreadyRedeemed: String = "<red>You have already redeemed the code '<dark_red>{0}</dark_red>'!",
    @SerializedName("max_redeems", alternate = ["maxRedeems"])
    val maxRedeems: String = "<red>The code '<dark_red>{0}</dark_red>' has reached the max redeems!",
    @SerializedName("code_redeemed", alternate = ["codeRedeemed"])
    val codeRedeemed: String = "<green>Successfully redeemed the code '<dark_red>{0}</dark_red>'!",
) {
    companion object {
        private fun parseMessage(message: String, vararg arguments: String): String {
            var modifiedMessage = message
            arguments.forEachIndexed { index, argument ->
                modifiedMessage = modifiedMessage.replace("{$index}", argument)
            }
            return modifiedMessage
        }

        fun sendMessage(audience: Audience, message: String, vararg arguments: String) {
            audience.sendMessage(TextUtils.toNative(parseMessage(message, *arguments)))
        }
    }

    override fun toString(): String {
        return "MessageSettings(invalidCode='$invalidCode', alreadyRedeemed='$alreadyRedeemed', maxRedeems='$maxRedeems', codeRedeemed='$codeRedeemed')"
    }
}
