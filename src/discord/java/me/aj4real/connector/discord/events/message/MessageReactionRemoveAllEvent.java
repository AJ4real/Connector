package me.aj4real.connector.discord.events.message;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.events.DiscordEvent;
import org.json.simple.JSONObject;

public class MessageReactionRemoveAllEvent extends DiscordEvent {

    public MessageReactionRemoveAllEvent(DiscordConnector c, JSONObject data) {
        super(c, data);
    }

}
