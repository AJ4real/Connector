package me.aj4real.connector.discord.events.interactions;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.events.DiscordEvent;
import org.json.simple.JSONObject;

public class ApplicationCommandUpdateEvent extends DiscordEvent {

    public ApplicationCommandUpdateEvent(DiscordConnector c, JSONObject data) {
        super(c, data);
    }

}
