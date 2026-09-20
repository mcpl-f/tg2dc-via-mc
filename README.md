# tg2dc via mc

A Spigot/Paper plugin that completes a three-way Minecraft Discord Telegram bridge: Discord <-> Minecraft <-> Telegram. It connects [tg-bridge](https://github.com/fulcanelly/mctg-bridge) with [DiscordSRV](https://www.spigotmc.org/resources/discordsrv.18494/) without a separate proxy service.

## What it does

- Discord messages are forwarded to the Telegram group attached to `tg-bridge`.
- Messages from that Telegram group are forwarded to the DiscordSRV main text channel.
- Telegram bot commands beginning with `/` are not forwarded to Discord.
- The adapter has no separate config file. It reads the Telegram chat from `tg-bridge` and the Discord destination from DiscordSRV.

## Requirements

Install these plugins on the same Minecraft server:

1. [Spigot](https://www.spigotmc.org/) or [Paper](https://papermc.io/downloads/paper) server.
2. [tg-bridge](https://github.com/fulcanelly/mctg-bridge/releases) `1.4.2` or a compatible version.
3. [DiscordSRV](https://www.spigotmc.org/resources/discordsrv.18494/) `1.29.0` or a compatible version.
4. Java 17.

The adapter declares both `tg-bridge` and `DiscordSRV` as required dependencies and will not load without them.

## Setup

### 1. Configure tg-bridge and Telegram

Configure [tg-bridge](https://github.com/fulcanelly/mctg-bridge) and attach the target Telegram group first.

The target Telegram group must be the group linked by `tg-bridge`; the adapter ignores messages from other Telegram chats.

### 2. Configure DiscordSRV

Configure [DiscordSRV](https://docs.discordsrv.com/installation/) using its official documentation and set its main text channel.

This adapter sends Telegram messages to the DiscordSRV main text channel. If that channel is unavailable, it tries DiscordSRV's configured game-channel destination.

### 3. Install the adapter

1. Download or build `tg-bridge-discord-via-mc`.
2. Copy its JAR to `plugins/`.
3. Restart the server after `tg-bridge` and DiscordSRV are configured.
4. Send a normal message in the linked Telegram group and in the Discord destination channel to verify both directions.

Build from this repository:

```bash
mvn clean package
```

The JAR is created in `target/`.

## Message examples

Discord to Telegram:

```text
Discord: Alex: Hello from Discord
```

Telegram to Discord:

```text
telegram: Alex: Hello from Telegram
```

Telegram commands such as `/list` and `/uptime` remain bot commands and are not mirrored to Discord.

## Verify installation

Send a normal message in each connected channel and confirm the three-way flow:

- Discord -> Minecraft and Telegram
- Minecraft -> Discord and Telegram
- Telegram -> Minecraft and Discord

If one direction is missing, check that:

- `tg-bridge` has a configured `chat_id` for the intended Telegram group.
- DiscordSRV is connected and has a configured main text channel or game-channel mapping.
- Both required plugins are installed and the server was restarted after configuration.

## Source

- [Main tg-bridge project](https://github.com/fulcanelly/mctg-bridge)
- [tg-bridge releases](https://github.com/fulcanelly/mctg-bridge/releases)
- [DiscordSRV documentation](https://docs.discordsrv.com/)
- [DiscordSRV source](https://github.com/DiscordSRV/DiscordSRV)
