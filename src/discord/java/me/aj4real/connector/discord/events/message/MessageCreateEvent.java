package me.aj4real.connector.discord.events.message;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.events.DiscordEvent;
import me.aj4real.connector.discord.objects.Message;
import org.json.simple.JSONObject;

public class MessageCreateEvent extends DiscordEvent {

    private final Message message;

    public MessageCreateEvent(DiscordConnector c, JSONObject data) {
        super(c, data);
        JSONObject d = (JSONObject) data.get("d");
        message = new Message(c, d);
    }
    public Message getMessage() {
        return this.message;
    }
}
