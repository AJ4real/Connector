package me.aj4real.connector.discord.objects.channel;

import me.aj4real.connector.discord.DiscordConnector;
import org.json.simple.JSONObject;

public class GuildChannelCategory extends GuildChannel {
    public GuildChannelCategory(DiscordConnector c, JSONObject data) {
        super(c, data);
    }

    /*
Example Channel Category
{
  "permission_overwrites": [],
}
     */
}
