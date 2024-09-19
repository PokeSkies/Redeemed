# Redeemed
<img width="100" height="50" src="https://i.imgur.com/c1DH9VL.png" alt="Requires Fabric Kotlin"/> <img src="https://img.shields.io/badge/Enviroment-Server-purple" alt="Enviroment Server"> <a href="https://discord.gg/invite/cgBww275Fg" rel="noopener nofollow ugc"><img src="https://img.shields.io/discord/1158447623989116980?color=blue&amp;logo=discord&amp;label=Discord" alt="Discord"></a>

A Fabric (1.20.1) server-sided code based redeeming system! Create "codes" that players use to redeem exclusive rewards.

More information on configuration can be found on the [Wiki](https://github.com/PokeSkies/Redeemed/wiki)!

## Features
- Create practically infinite codes *(idk, haven't tested that)*
- Set the maximum number of total times a code can be used

## Installation
1. Download the latest version of the mod from the Releases tab.
2. Download all required dependencies:
    - [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin)
    - [Fabric Permissions API](https://github.com/PokeSkies/fabric-permissions-api)
3. Install the mod and dependencies into your server's `mods` folder.
4. Configure your codes in the `./config/redeemed/config.json` file.

## Commands/Permissions
| Command                     | Description                                                   | Permission                 |
|-----------------------------|---------------------------------------------------------------|----------------------------|
| /redeemed                 | Base Mod command                                                | redeemed.command.redeemed   |
| /redeemed reload                 | Reload the Mod                                                | redeemed.command.reload   |
| /redeemed list               | List out all of the created codes and their current uses       | redeemed.command.printnbt |
| /redeemed debug                  | Toggle the debug mode for more insight into errors            | redeemed.command.deug     |
| /redeem `<code>`         | Attempt to claim a code            | redeemed.command.redeem     |

## Support
A community support Discord has been opened up for all Skies Development related projects! Feel free to join and ask questions or leave suggestions :)

<a class="discord-widget" href="https://discord.gg/cgBww275Fg" title="Join us on Discord"><img src="https://discordapp.com/api/guilds/1158447623989116980/embed.png?style=banner2"></a>
