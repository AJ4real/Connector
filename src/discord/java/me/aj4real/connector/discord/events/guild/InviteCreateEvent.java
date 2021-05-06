package me.aj4real.connector.discord.events.guild;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.events.DiscordEvent;
import org.json.simple.JSONObject;

public class InviteCreateEvent extends DiscordEvent {

    public InviteCreateEvent(DiscordConnector c, JSONObject data) {
        super(c, data);
    }

}
