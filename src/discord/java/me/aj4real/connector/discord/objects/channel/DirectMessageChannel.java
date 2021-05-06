package me.aj4real.connector.discord.objects.channel;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.DiscordEndpoints;
import me.aj4real.connector.discord.objects.Snowflake;
import me.aj4real.connector.discord.objects.Channel;
import me.aj4real.connector.discord.objects.User;
import me.aj4real.connector.paginators.Paginator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;

public class DirectMessageChannel extends Channel {
    private Snowflake lastMessageId;
    public DirectMessageChannel(DiscordConnector c, JSONObject data) {
        super(c, data);
        this.lastMessageId = Snowflake.of((String) data.get("last_message_id"));
    }
    public Snowflake getLastMessageId() {
        return this.lastMessageId;
    }
    public Paginator<User> listRecipients() {
        JSONArray arr = (JSONArray) data.get("recipients");
        return Paginator.of((i) -> {
            try {
                return new User(c, (JSONObject) c.readJson(DiscordEndpoints.USER.fulfil("user_id", (String) ((JSONObject)arr.get(i.intValue())).get("id"))).getData());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
}
