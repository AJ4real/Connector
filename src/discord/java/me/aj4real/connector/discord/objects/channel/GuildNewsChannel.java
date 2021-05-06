package me.aj4real.connector.discord.objects.channel;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.objects.Snowflake;
import org.json.simple.JSONObject;

public class GuildNewsChannel extends GuildChannel {
    private String topic;
    private Snowflake lastMessageId;
    public GuildNewsChannel(DiscordConnector c, JSONObject data) {
        super(c, data);
        this.topic = (String) data.get("topic");
        this.lastMessageId = Snowflake.of((String) data.get("last_message_id"));
    }
    public String getTopic() {
        return this.topic;
    }
    public Snowflake getLastMessageId() {
        return this.lastMessageId;
    }
}
