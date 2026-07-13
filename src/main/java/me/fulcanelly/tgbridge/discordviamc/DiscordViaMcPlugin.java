package me.fulcanelly.tgbridge.discordviamc;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.eventbus.EventBus;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import me.fulcanelly.tgbridge.Bridge;
import me.fulcanelly.tgbridge.tools.MainConfig;
import me.fulcanelly.tgbridge.tools.MessageSender;

public final class DiscordViaMcPlugin extends JavaPlugin {

    private static final String BRIDGE_PLUGIN_NAME = "tg-bridge";

    private DiscordToTelegramListener discordListener;
    private TelegramToDiscordListener telegramListener;
    private EventBus telegramEventBus;

    @Override
    public void onEnable() {
        // initialize();
        getServer().getScheduler().runTaskLater(this, this::initialize, 200L);

    }

    private void initialize() {

        Bridge bridge = (Bridge) getServer().getPluginManager().getPlugin(BRIDGE_PLUGIN_NAME);
        MessageSender sender = bridge.getInjector().getInstance(MessageSender.class);
        MainConfig mainConfig = bridge.getInjector().getInstance(MainConfig.class);
        TextChannel discordChannel = resolveDiscordChannel();

        if (isConfigured(mainConfig.getChatId())) {
            discordListener = new DiscordToTelegramListener(sender);
            DiscordSRV.api.subscribe(discordListener);
        } else {
            getLogger().warning("tg-bridge chat_id is not configured; Discord to Telegram mirror is disabled");
        }

        if (discordChannel != null) {

            telegramEventBus = bridge.getInjector().getInstance(EventBus.class);
            telegramListener = new TelegramToDiscordListener(discordChannel, mainConfig);
            telegramEventBus.register(telegramListener);
        } else {
            getLogger()
                    .warning("DiscordSRV main text channel is not configured; Telegram to Discord mirror is disabled");
        }
    }

    @Override
    public void onDisable() {
        if (discordListener != null) {
            DiscordSRV.api.unsubscribe(discordListener);
        }
        if (telegramEventBus != null && telegramListener != null) {
            telegramEventBus.unregister(telegramListener);
        }
    }

    private boolean isConfigured(String value) {
        return value != null && !value.isBlank();
    }

    private TextChannel resolveDiscordChannel() {
        DiscordSRV discord = DiscordSRV.getPlugin();

        TextChannel channel = discord.getMainTextChannel();
        if (channel != null) {
            return channel;
        }

        String mainChatChannel = discord.getMainChatChannel();
        if (isConfigured(mainChatChannel)) {
            channel = discord.getDestinationTextChannelForGameChannelName(mainChatChannel);
            if (channel != null) {
                return channel;
            }
        }

        return discord.getChannels()
                .values()
                .stream()
                .filter(this::isConfigured)
                .map(DiscordUtil::getTextChannelById)
                .filter(candidate -> candidate != null)
                .findFirst()
                .orElse(null);
    }
}
