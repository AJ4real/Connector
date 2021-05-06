package me.aj4real.connector.discord.exceptions;

import me.aj4real.connector.discord.DiscordConnector;
import org.json.simple.JSONObject;

import java.io.IOException;

public class DiscordRestException extends IOException {
    protected JSONObject data;
    DiscordConnector c;
    public DiscordRestException(DiscordConnector c, JSONObject data) throws BotPermissionsException {
        super("Discords REST responded with code " + data.get("code") + ": " + data.get("message"));
        this.c = c;
        this.data = data;
    }
    public JSONObject getResponse() {
        return this.data;
    }
}
