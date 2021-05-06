package me.aj4real.connector.discord.events.guild;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.events.DiscordEvent;
import org.json.simple.JSONObject;

public class GuildMemberRemoveEvent extends DiscordEvent {

    public GuildMemberRemoveEvent(DiscordConnector c, JSONObject data) {
        super(c, data);
    }

}
