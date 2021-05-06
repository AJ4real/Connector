package me.aj4real.connector.discord.objects.channel;

import me.aj4real.connector.Logger;
import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.DiscordEndpoints;
import me.aj4real.connector.discord.objects.Message;
import me.aj4real.connector.discord.objects.Snowflake;
import me.aj4real.connector.discord.specs.CreateMessageSpec;
import org.json.simple.JSONObject;

import java.util.Optional;
import java.util.function.Consumer;

public class GuildTextChannel extends GuildChannel {
    private String topic;
    private Optional<Snowflake> lastMessageId = Optional.empty();
    private long rateLimitPerUser;
    public GuildTextChannel(DiscordConnector c, JSONObject data) {
        super(c, data);
        this.topic = (String) data.get("topic");
        try {
            this.lastMessageId = Optional.of(Snowflake.of((String) data.get("last_message_id")));
        } catch (Exception e) {}
        this.rateLimitPerUser = (Long) data.get("rate_limit_per_user");
    }
    public Message createMessage(Consumer<CreateMessageSpec> spec) {
        CreateMessageSpec mutatedSpec = new CreateMessageSpec(DiscordEndpoints.CREATE_MESSAGE.fulfil("channel_id", getId().asString()));
        spec.accept(mutatedSpec);
        try {
            JSONObject o = (JSONObject) c.sendRequest(mutatedSpec).getData();
            return new Message(c, o);
        }catch (Exception e) {
            Logger.handle(e);
            return null;
        }
    }
    public String getTopic() {
        return this.topic;
    }
    public Optional<Snowflake> getLastMessageId() {
        return this.lastMessageId;
    }
    public long getRateLimitPerUser() {
        return this.rateLimitPerUser;
    }
}
