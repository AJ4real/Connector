package me.aj4real.connector.discord.objects.channel;

import me.aj4real.connector.discord.DiscordConnector;
import org.json.simple.JSONObject;

public class GuildStoreChannel extends GuildChannel {
    public GuildStoreChannel(DiscordConnector c, JSONObject data) {
        super(c, data);
    }
}
