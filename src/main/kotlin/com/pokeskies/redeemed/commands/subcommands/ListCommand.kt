package com.pokeskies.redeemed.commands.subcommands

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import com.pokeskies.redeemed.Redeemed
import com.pokeskies.redeemed.config.ConfigManager
import com.pokeskies.redeemed.data.CodeData
import com.pokeskies.redeemed.utils.SubCommand
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component

class ListCommand : SubCommand {
    override fun build(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("list")
            .requires(Permissions.require("${Redeemed.MOD_ID}.command.list", 2))
            .executes(Companion::reload)
            .build()
    }

    companion object {
        fun reload(ctx: CommandContext<CommandSourceStack>): Int {
            val storage = Redeemed.INSTANCE.storage
            if (storage == null) {
                ctx.source.sendSystemMessage(
                    Component.literal("Storage service is not available!")
                        .withStyle { it.withColor(ChatFormatting.RED) }
                )
                return 1
            }

            if (ConfigManager.CONFIG.codes.size <= 0) {
                ctx.source.sendSystemMessage(
                    Component.literal("No codes have been created yet!")
                        .withStyle { it.withColor(ChatFormatting.RED) }
                )
                return 1
            }

            ctx.source.sendSystemMessage(
                Component.literal("Codes:")
                    .withStyle { it.withColor(ChatFormatting.GOLD).withBold(true) }
            )
            ConfigManager.CONFIG.codes.forEach { (id, config) ->
                val codeData = storage.getCodeData(id) ?: CodeData()
                ctx.source.sendSystemMessage(Component.literal(" - ").withStyle { it.withColor(ChatFormatting.GRAY) }
                    .append(Component.literal(id).withStyle { it.withColor(ChatFormatting.AQUA) })
                    .append(Component.literal(" | ").withStyle { it.withColor(ChatFormatting.GRAY) })
                    .append(Component.literal("${codeData.uses}/${config.getMaxUses()} Uses").withStyle { it.withColor(ChatFormatting.GREEN) })
                )
            }
            return 1
        }
    }
}
