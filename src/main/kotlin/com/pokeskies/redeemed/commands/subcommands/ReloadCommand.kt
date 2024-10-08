package com.pokeskies.redeemed.commands.subcommands

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import com.pokeskies.redeemed.Redeemed
import com.pokeskies.redeemed.utils.SubCommand
import me.lucko.fabric.api.permissions.v0.Permissions
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

class ReloadCommand : SubCommand {
    override fun build(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("reload")
            .requires(Permissions.require("${Redeemed.MOD_ID}.command.reload", 2))
            .executes(Companion::reload)
            .build()
    }

    companion object {
        fun reload(ctx: CommandContext<CommandSourceStack>): Int {
            Redeemed.INSTANCE.reload()
            ctx.source.sendMessage(Component.text("Reloaded ${Redeemed.MOD_NAME}!").color(NamedTextColor.GREEN))
            return 1
        }
    }
}
