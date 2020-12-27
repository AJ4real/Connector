package me.aj4real.connector.discord;

import me.aj4real.connector.Connector;

public class DiscordConnector extends Connector {
    public static String AUTHORIZE = "https://discord.com/api/oauth2/authorize";

    public void setAuthenticationToken(String token) {
        auth = "Bot " + token;
    }
}
