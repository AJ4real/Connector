package me.aj4real.connector.discord.events.channel;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.events.DiscordEvent;
import org.json.simple.JSONObject;

public class ChannelUdpateEvent extends DiscordEvent {

    public ChannelUdpateEvent(DiscordConnector c, JSONObject data) {
        super(c, data);
    }

}
