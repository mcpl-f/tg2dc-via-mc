package me.fulcanelly.tgbridge.discordviamc;

import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
import me.fulcanelly.tgbridge.tools.MessageSender;

public final class DiscordToTelegramListener {

    private final MessageSender sender;

    public DiscordToTelegramListener(MessageSender sender) {
        this.sender = sender;
    }

    @Subscribe
    public void onDiscordMessage(DiscordGuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        String author = event.getAuthor().getName();

        sender.sendAsPlayer("discord: " + author, message);

    }
}
