package com.pokeskies.redeemed.utils

import com.pokeskies.redeemed.Redeemed
import net.minecraft.network.chat.Component

object TextUtils {
    fun toNative(text: String): Component {
        return Redeemed.INSTANCE.adventure.toNative(Redeemed.MINI_MESSAGE.deserialize(text))
    }

    fun toComponent(text: String): net.kyori.adventure.text.Component {
        return Redeemed.MINI_MESSAGE.deserialize(text)
    }
}
