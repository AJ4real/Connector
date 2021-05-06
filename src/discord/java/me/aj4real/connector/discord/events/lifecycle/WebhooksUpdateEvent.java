package me.aj4real.connector.discord.events.lifecycle;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.events.DiscordEvent;
import org.json.simple.JSONObject;

public class WebhooksUpdateEvent extends DiscordEvent {

    public WebhooksUpdateEvent(DiscordConnector c, JSONObject data) {
        super(c, data);
    }

}
