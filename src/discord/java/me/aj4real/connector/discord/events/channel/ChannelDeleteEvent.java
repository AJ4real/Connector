package me.aj4real.connector.discord.events.channel;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.events.DiscordEvent;
import org.json.simple.JSONObject;

public class ChannelDeleteEvent extends DiscordEvent {

    public ChannelDeleteEvent(DiscordConnector c, JSONObject data) {
        super(c, data);
    }

}
