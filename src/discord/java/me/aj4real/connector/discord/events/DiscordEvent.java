package me.aj4real.connector.discord.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.events.Event;
import org.json.simple.JSONObject;

public class DiscordEvent extends Event {
    public DiscordEvent(Connector c, JSONObject data) {
        super(c, data);
    }
}
