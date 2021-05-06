package me.aj4real.connector.discord.objects;

import me.aj4real.connector.Request;
import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.DiscordEndpoints;
import me.aj4real.connector.discord.RequiresDiscordPermission;
import me.aj4real.connector.discord.objects.channel.*;
import org.json.simple.JSONObject;

import java.lang.reflect.InvocationTargetException;

public class Channel {
    protected DiscordConnector c;
    protected JSONObject data;
    private Snowflake id;
    private Type type;
    public Channel(DiscordConnector c, JSONObject data) {
        this.c = c;
        this.data = data;
        this.id = Snowflake.of((String) data.get("id"));
        this.type = Type.of((Long) data.get("type"));
    }

    @RequiresDiscordPermission(permission = Role.Permission.MANAGE_WEBHOOKS)
    public Webhook createWebhook(String name) {
        try {
            JSONObject p = new JSONObject();
            p.put("name", name);
            JSONObject o = (JSONObject) c.readJson(DiscordEndpoints.CREATE_CHANNEL_WEBHOOK.fulfil("channel_id", id.asString()), p.toString()).getData();
            Webhook webhook = new Webhook(c, o);
            return webhook;
        } catch (Exception e) {}
        return null;
    }
    public Snowflake getId() {
        return this.id;
    }
    public Type getType() {
        return this.type;
    }
    public static <T extends Channel> T valueOf(DiscordConnector c, JSONObject data) {
        Class cl = Type.of((Long)data.get("type")).getChannelClass();
        try {
            T channel = (T) cl.getConstructor(DiscordConnector.class, JSONObject.class).newInstance(c, data);
            return channel;
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    };
    public enum Type {
        GUILD_TEXT(0, GuildTextChannel.class),
        DM(1, DirectMessageChannel.class),
        GUILD_VOICE(2, GuildVoiceChannel.class),
        GROUP_DM(3, GroupDirectMessageChannel.class),
        GUILD_CATEGORY(4, GuildChannelCategory.class),
        GUILD_NEWS(5, GuildNewsChannel.class),
        GUILD_STORE(6, GuildStoreChannel.class);

        long id;
        Class clazz;

        Type(long id, Class clazz) {
            this.id = id;
            this.clazz = clazz;
        }

        public long getId() {
            return this.id;
        }
        public Class getChannelClass() {
            return this.clazz;
        }
        public static Type of(int id) {
            return of(((Integer)id).longValue());
        }
        public static Type of(long id) {
            for (Type value : Type.values()) {
                if(value.id == id) {
                    return value;
                }
            }
            return null;
        }
    }
}
