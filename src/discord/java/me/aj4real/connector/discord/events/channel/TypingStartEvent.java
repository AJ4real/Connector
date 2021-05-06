package me.aj4real.connector.discord.events.channel;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.events.DiscordEvent;
import org.json.simple.JSONObject;

public class TypingStartEvent extends DiscordEvent {

    public TypingStartEvent(DiscordConnector c, JSONObject data) {
        super(c, data);
    }

}
