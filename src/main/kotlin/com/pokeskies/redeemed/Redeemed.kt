package com.pokeskies.redeemed

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pokeskies.redeemed.commands.BaseCommand
import com.pokeskies.redeemed.commands.RedeemCommand
import com.pokeskies.redeemed.config.ConfigManager
import com.pokeskies.redeemed.storage.IStorage
import com.pokeskies.redeemed.utils.Utils
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.loader.api.FabricLoader
import net.kyori.adventure.platform.fabric.FabricServerAudiences
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.io.IOException

class Redeemed : ModInitializer {
    companion object {
        lateinit var INSTANCE: Redeemed

        var MOD_ID = "redeemed"
        var MOD_NAME = "Redeemed"

        val LOGGER: Logger = LogManager.getLogger(MOD_ID)
        val MINI_MESSAGE: MiniMessage = MiniMessage.miniMessage()

        @JvmStatic
        fun asResource(path: String): ResourceLocation {
            return ResourceLocation(MOD_ID, path)
        }
    }

    lateinit var configDir: File
    var storage: IStorage? = null

    lateinit var adventure: FabricServerAudiences
    var server: MinecraftServer? = null

    var gson: Gson = GsonBuilder().disableHtmlEscaping().create()

    var gsonPretty: Gson = gson.newBuilder().setPrettyPrinting().create()
    override fun onInitialize() {
        INSTANCE = this

        this.configDir = File(FabricLoader.getInstance().configDirectory, MOD_ID)
        ConfigManager.load()
        try {
            this.storage = IStorage.load(ConfigManager.CONFIG.storage)
        } catch (e: IOException) {
            Utils.printError(e.message)
            this.storage = null
        }

        registerEvents()
    }

    private fun registerEvents() {
        ServerLifecycleEvents.SERVER_STARTING.register(ServerLifecycleEvents.ServerStarting { server: MinecraftServer? ->
            this.adventure = FabricServerAudiences.of(
                server!!
            )
            this.server = server
        })
        ServerLifecycleEvents.SERVER_STOPPED.register(ServerLifecycleEvents.ServerStopped { server: MinecraftServer? ->
            this.storage?.close()
        })
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            BaseCommand().register(dispatcher)
            RedeemCommand().register(dispatcher)
        }
    }

    fun reload() {
        this.storage?.close()

        ConfigManager.load()

        try {
            this.storage = IStorage.load(ConfigManager.CONFIG.storage)
        } catch (e: IOException) {
            Utils.printError(e.message)
            this.storage = null
        }
    }
}
