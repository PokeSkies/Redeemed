package com.pokeskies.redeemed.config

class RedeemedConfig(
    var debug: Boolean = false,
    val storage: StorageSettings = StorageSettings(),
    val messages: MessageSettings = MessageSettings(),
    val codes: MutableMap<String, CodeInstance> = mutableMapOf()
) {
    override fun toString(): String {
        return "RedeemedConfig(debug=$debug, storage=$storage, messages=$messages, codes=$codes)"
    }
}
