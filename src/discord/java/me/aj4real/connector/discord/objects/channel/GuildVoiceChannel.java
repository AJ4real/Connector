package me.aj4real.connector.discord.objects.channel;

import me.aj4real.connector.discord.DiscordConnector;
import org.json.simple.JSONObject;

public class GuildVoiceChannel extends GuildChannel{
    private long bitrate, userLimit;
    public GuildVoiceChannel(DiscordConnector c, JSONObject data) {
        super(c, data);
        this.bitrate = (Long) data.get("bitrate");
        this.userLimit = (Long) data.get("user_limit");
    }
    public long getBitrate() {
        return this.bitrate;
    }
    public long getUserLimit() {
        return this.userLimit;
    }
}
