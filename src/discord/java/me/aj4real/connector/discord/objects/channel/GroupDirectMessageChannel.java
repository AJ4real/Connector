package me.aj4real.connector.discord.objects.channel;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.objects.Snowflake;
import org.json.simple.JSONObject;

public class GroupDirectMessageChannel extends DirectMessageChannel {
    private String name;
    private Snowflake ownerId;
    public GroupDirectMessageChannel(DiscordConnector c, JSONObject data) {
        super(c, data);
        this.name = (String) data.get("name");
        this.ownerId = Snowflake.of((String) data.get("owner_id"));
    }
    public String getName() {
        return this.name;
    }
    public Snowflake getOwnerId() {
        return this.ownerId;
    }
}
