package me.aj4real.connector.discord.events.message;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.events.DiscordEvent;
import org.json.simple.JSONObject;

public class MessageReactionRemoveEmojiEvent extends DiscordEvent {

    public MessageReactionRemoveEmojiEvent(DiscordConnector c, JSONObject data) {
        super(c, data);
    }

}
