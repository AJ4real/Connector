package me.aj4real.connector.discord.objects;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.DiscordEndpoints;
import me.aj4real.connector.discord.RequiresDiscordPermission;
import me.aj4real.connector.discord.specs.InvokeWebhookSpec;
import me.aj4real.connector.discord.specs.ModifyWebhookSpec;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

public class Webhook {
    protected final DiscordConnector c;
    protected final JSONObject data;
    private final Snowflake id, guildId, channelId;
    private final String token, name;
    private final Optional<Snowflake> appId, userId;
    private final Optional<User> user;
    private final Optional<String> username;
    private final Type type;

    public Webhook(DiscordConnector c, JSONObject data) {
        this.c = c;
        this.data = data;
        id = Snowflake.of((String) data.get("id"));
        guildId = Snowflake.of((String) data.get("guild_id"));
        channelId = Snowflake.of((String) data.get("channel_id"));
        token = (String) data.get("token");
        name = (String) data.get("name");
        JSONObject u = (JSONObject) data.get("user");
        user = u != null ? Optional.of(new User(c, u)) : Optional.empty();
        userId = u != null ? Optional.of(Snowflake.of((String) u.get("id"))) : Optional.empty();
        username = u != null ? Optional.of((String)u.get("username")) : Optional.empty();
        type = Type.valueOf((Long)data.get("type"));
        appId = data.get("application_id") != null ? Optional.of(Snowflake.of((String) data.get("application_id"))) : Optional.empty();
    }

    @RequiresDiscordPermission(permission = Role.Permission.MANAGE_WEBHOOKS)
    public Webhook modify(Consumer<ModifyWebhookSpec> spec) {
        ModifyWebhookSpec mutatedSpec = new ModifyWebhookSpec(DiscordEndpoints.MODIFY_WEBHOOK.fulfil("webhook_id", this.id.asString()));
        spec.accept(mutatedSpec);
        try {
            JSONObject channel = (JSONObject) c.sendRequest(mutatedSpec).getData();
            return new Webhook(c, channel);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Webhook modifyWithToken(Consumer<ModifyWebhookSpec> spec) {
        ModifyWebhookSpec mutatedSpec = new ModifyWebhookSpec(DiscordEndpoints.MODIFY_WEBHOOK_WITH_TOKEN.fulfil("webhook_id", this.id.asString()));
        spec.accept(mutatedSpec);
        try {
            JSONObject channel = (JSONObject) c.sendRequest(mutatedSpec).getData();
            return new Webhook(c, channel);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequiresDiscordPermission(permission = Role.Permission.MANAGE_WEBHOOKS)
    public void execute(Consumer<InvokeWebhookSpec> spec) {
        InvokeWebhookSpec mutatedSpec = new InvokeWebhookSpec(DiscordEndpoints.INVOKE_WEBHOOK.fulfil("webhook_id", this.id.asString()).fulfil("token", this.token));
        spec.accept(mutatedSpec);
        try {
            JSONObject msg = (JSONObject) c.sendRequest(mutatedSpec).getData();
//            System.out.println("message: " + msg.toString()); TODO
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresDiscordPermission(permission = Role.Permission.MANAGE_WEBHOOKS)
    public void delete() {
        try {
            JSONObject o = (JSONObject) c.readJson(DiscordEndpoints.DELETE_WEBHOOK.fulfil("webhook_id", id.asString())).getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteWithToken() {
        try {
            JSONObject o = (JSONObject) c.readJson(DiscordEndpoints.DELETE_WEBHOOK_WITH_TOKEN.fulfil("webhook_id", id.asString()).fulfil("token", token)).getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Guild getGuild() {
        try {
            return c.getDiscord().fetchGuild(guildId);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Channel getChannel() {
        try {
            return c.getDiscord().fetchChannel(channelId);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Type getType() {
        return type;
    }
    public Optional<User> getUser() {
        return user;
    }
    public String getToken() {
         return token;
    }
    public Optional<String> getUsername() {
        return username;
    }
    public String getName() {
         return name;
    }
    public Optional<Snowflake> getAppId() {
        return appId;
    }
    public Snowflake getId() {
        return id;
    }
    public Snowflake getGuildId() {
        return guildId;
    }
    public Snowflake getChannelId() {
        return channelId;
    }
    public Optional<Snowflake> getUserId() {
        return userId;
    }

    public enum Type {
        INCOMING(1, "Incoming Webhooks can post messages to channels with a generated token"),
        CHANNEL_FOLLOWER(2, "Channel Follower Webhooks are internal webhooks used with Channel Following to post new messages into channels");

        private final long id;
        private final String description;
        Type(long id, String description) {
            this.id = id;
            this.description = description;
        }
        public long getId() {
            return this.id;
        }
        public String getDescription() {
            return description;
        }
        public static Type valueOf(long id) {
            for(Type type : values()) {
                if(type.getId() == id) {
                    return type;
                }
            }
            return null;
        }
    }
}
