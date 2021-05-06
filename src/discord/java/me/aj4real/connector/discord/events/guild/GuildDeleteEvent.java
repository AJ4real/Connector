package me.aj4real.connector.discord.events.guild;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.events.DiscordEvent;
import org.json.simple.JSONObject;

public class GuildDeleteEvent extends DiscordEvent {

    public GuildDeleteEvent(DiscordConnector c, JSONObject data) {
        super(c, data);
    }

}
