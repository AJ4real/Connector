package me.aj4real.connector.discord.events.lifecycle;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.events.DiscordEvent;
import org.json.simple.JSONObject;

public class PresenceUpdateEvent extends DiscordEvent {

    public PresenceUpdateEvent(DiscordConnector c, JSONObject data) {
        super(c, data);
    }

}
