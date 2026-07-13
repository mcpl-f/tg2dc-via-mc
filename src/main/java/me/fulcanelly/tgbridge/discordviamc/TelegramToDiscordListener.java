package me.fulcanelly.tgbridge.discordviamc;

import java.util.Objects;

import com.google.common.eventbus.Subscribe;

import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import me.fulcanelly.tgbridge.tapi.Message;
import me.fulcanelly.tgbridge.tools.MainConfig;

public final class TelegramToDiscordListener {

    private final TextChannel discordChannel;
    private final MainConfig mainConfig;

    public TelegramToDiscordListener(TextChannel discordChannel, MainConfig mainConfig) {
        this.discordChannel = discordChannel;
        this.mainConfig = mainConfig;
    }

    @Subscribe
    public void onTelegramMessage(Message event) {
        String message = event.getText();
        if (message == null || message.startsWith("/") || !isPinnedTelegramChat(event)) {
            return;
        }

        discordChannel.sendMessage(String.format(" telegram: %s: %s", event.getFrom().getName(), message)).queue();
    }

    private boolean isPinnedTelegramChat(Message event) {
        return Objects.equals(event.getChat().getId().toString(), mainConfig.getChatId());
    }
}
