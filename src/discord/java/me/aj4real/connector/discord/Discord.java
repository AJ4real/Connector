package me.aj4real.connector.discord;

import me.aj4real.connector.Logger;
import me.aj4real.connector.discord.commands.CommandRegistry;
import me.aj4real.connector.discord.objects.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

public class Discord {

    private DiscordConnector c;
    private CommandRegistry cm;

    public Discord(DiscordConnector c) {
        this.c = c;
        this.cm = new CommandRegistry(c);
    }

    public CommandRegistry getCommandHandler() {
        return this.cm;
    }

    public Bot fetchBot() throws IOException {
        return new Bot(c, (JSONObject) c.readJson(DiscordEndpoints.BOT).getData());
    }

    public User fetchBotUser() throws IOException {
        return new User(c, (JSONObject) c.readJson(DiscordEndpoints.USER.fulfil("user_id", getAppId())).getData());
    }

    public Member fetchBotMember(Snowflake guildId) throws IOException {
        return fetchBotMember(guildId.asString());
    }
    public Member fetchBotMember(String guildId) throws IOException {
        JSONObject o = (JSONObject) c.readJson(DiscordEndpoints.GUILD_MEMBER.fulfil("guild_id", guildId).fulfil("user_id", getAppId())).getData();
        return new Member(Snowflake.of(guildId), c, o);
    }

    public Guild fetchGuild(Snowflake guildId) throws IOException {
        return fetchGuild(guildId.asString());
    }
    public Guild fetchGuild(String guildId) throws IOException {
        return new Guild(c, (JSONObject) c.readJson(DiscordEndpoints.GUILDS.fulfil("guild_id", guildId)).getData());
    }

    public Channel fetchChannel(Snowflake channelId) throws IOException {
        return fetchChannel(channelId.asString());
    }
    public Channel fetchChannel(String channelId) throws IOException {
        return Channel.valueOf(c, (JSONObject) c.readJson(DiscordEndpoints.CHANNEL.fulfil("channel_id", channelId)).getData());
    }

    @RequiresDiscordPermission(permission = Role.Permission.MANAGE_WEBHOOKS)
    public Webhook[] fetchChannelWebhooks(String channelId) throws IOException {
        JSONArray arr = (JSONArray) c.readJson(DiscordEndpoints.CHANNEL_WEBHOOKS.fulfil("channel_id", channelId)).getData();
        Webhook[] webhooks = new Webhook[arr.size()];
        for(int i = 0; i < arr.size(); i++) {
            JSONObject d = (JSONObject) arr.get(i);
            webhooks[i] = new Webhook(c, d);
        }
        return webhooks;
    }

    @RequiresDiscordPermission(permission = Role.Permission.MANAGE_WEBHOOKS)
    public Webhook[] fetchGuildWebhooks(String guildId) throws IOException {
        JSONArray arr = (JSONArray) c.readJson(DiscordEndpoints.GUILD_WEBHOOKS.fulfil("channel_id", guildId)).getData();
        Webhook[] webhooks = new Webhook[arr.size()];
        for(int i = 0; i < arr.size(); i++) {
            JSONObject d = (JSONObject) arr.get(i);
            webhooks[i] = new Webhook(c, d);
        }
        return webhooks;
    }

    @RequiresDiscordPermission(permission = Role.Permission.MANAGE_WEBHOOKS)
    public Webhook fetchWebhook(String webhookId) throws IOException {
        JSONObject d = (JSONObject) c.readJson(DiscordEndpoints.WEBHOOK.fulfil("webhook_id", webhookId)).getData();
        return new Webhook(c, d);
    }

    public Webhook fetchWebhook(String webhookId, String token) throws IOException {
        JSONObject d = (JSONObject) c.readJson(DiscordEndpoints.WEBHOOK_WITH_TOKEN.fulfil("webhook_id", webhookId).fulfil("token", token)).getData();
        return new Webhook(c, d);
    }

    public User fetchUser(Snowflake userId) throws IOException {
        return fetchUser(userId.asString());
    }
    public User fetchUser(String userId) throws IOException {
        return new User(c, (JSONObject) c.readJson(DiscordEndpoints.USER.fulfil("user_id", userId)).getData());
    }

    public Member fetchGuildMember(Snowflake guildId, Snowflake userId) throws IOException {
        return fetchGuildMember(guildId.asString(), userId.asString());
    }
    public Member fetchGuildMember(String guildId, String userId) throws IOException {
        JSONObject o = (JSONObject) c.readJson(DiscordEndpoints.GUILD_MEMBER.fulfil("guild_id", guildId).fulfil("user_id", userId)).getData();
        return new Member(Snowflake.of(guildId), c, o);
    }

    public String getSessionId() {
        return this.c.getSessionId();
    }

    public String getAppId() {
        return this.c.getAppId();
    }

}
