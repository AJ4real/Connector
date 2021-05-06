package me.aj4real.connector.discord.events.channel;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.events.DiscordEvent;
import org.json.simple.JSONObject;

public class ChannelCreateEvent extends DiscordEvent {

    public ChannelCreateEvent(DiscordConnector c, JSONObject data) {
        super(c, data);
    }

}
