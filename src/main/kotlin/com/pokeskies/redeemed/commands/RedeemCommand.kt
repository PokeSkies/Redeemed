package com.pokeskies.redeemed.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import com.pokeskies.redeemed.Redeemed
import com.pokeskies.redeemed.config.ConfigManager
import com.pokeskies.redeemed.config.MessageSettings
import com.pokeskies.redeemed.data.CodeData
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component

class RedeemCommand {
    private val aliases = listOf("redeem")

    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val rootCommands: List<LiteralCommandNode<CommandSourceStack>> = aliases.map {
            Commands.literal(it)
                .requires(Permissions.require("${Redeemed.MOD_ID}.command.redeem", 2))
                .then(Commands.argument("code", StringArgumentType.string())
                    .executes { ctx ->
                        execute(ctx, StringArgumentType.getString(ctx, "code"))
                    }
                )
                .build()
        }

        rootCommands.forEach { root ->
            dispatcher.root.addChild(root)
        }
    }

    companion object {
        fun execute(
            ctx: CommandContext<CommandSourceStack>,
            code: String
        ): Int {
            val player = ctx.source.player
            if (player == null) {
                ctx.source.sendSystemMessage(
                    Component.literal("You must be a player to run this command")
                        .withStyle { it.withColor(ChatFormatting.RED) }
                )
                return 1
            }

            val codeEntry = ConfigManager.CONFIG.codes[code]

            if (codeEntry == null) {
                MessageSettings.sendMessage(player, ConfigManager.CONFIG.messages.invalidCode, code)
                return 1
            }

            val storage = Redeemed.INSTANCE.storage

            if (storage == null) {
                ctx.source.sendSystemMessage(
                    Component.literal("Storage is not initialized! Please contact the server administrator.")
                        .withStyle { it.withColor(ChatFormatting.RED) }
                )
                return 1
            }

            val userData = storage.getUser(player.uuid)

            if (userData.usedCodes.contains(code)) {
                MessageSettings.sendMessage(player, ConfigManager.CONFIG.messages.alreadyRedeemed, code)
                return 1
            }

            val codeData = storage.getCodeData(code) ?: CodeData()

            if (codeEntry.maxUses > 0 && codeData.uses >= codeEntry.maxUses) {
                MessageSettings.sendMessage(player, ConfigManager.CONFIG.messages.maxRedeems, code)
                return 1
            }

            userData.usedCodes.add(code)
            if (!storage.saveUser(player.uuid, userData)) {
                ctx.source.sendSystemMessage(
                    Component.literal("An error occurred while saving your data! Please try again later.")
                        .withStyle { it.withColor(ChatFormatting.RED) }
                )
                return 1
            }

            codeData.uses++
            if (!storage.saveCodeData(code, codeData)) {
                ctx.source.sendSystemMessage(
                    Component.literal("An error occurred while saving data! Please try again later.")
                        .withStyle { it.withColor(ChatFormatting.RED) }
                )
                return 1
            }

            codeEntry.commands.forEach { it ->
                ctx.source.server.createCommandSourceStack().let { source ->
                    source.server.commands.performPrefixedCommand(source, it.replace("%player%", player.name.string))
                }
            }
            MessageSettings.sendMessage(player, ConfigManager.CONFIG.messages.codeRedeemed, code)

            return 1
        }
    }
}
