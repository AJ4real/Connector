package me.aj4real.connector.discord.objects;

import me.aj4real.connector.Task;
import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.DiscordEndpoints;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Optional;

public class Message {
    protected DiscordConnector c;
    protected JSONObject data;
    private final boolean mentionedEveryone, pinned, tts;
    private final Snowflake id, channelId, authorId;
    private final Optional<Snowflake> guildId;
    private final User author;
    private final Type type;
    private final Flag flag;
    private final String content, nonce;

    public Message(DiscordConnector c, JSONObject data) {
        this.c = c;
        this.data = data;
        mentionedEveryone = (boolean) data.get("mention_everyone");
        pinned = (boolean) data.get("pinned");
        tts = (boolean) data.get("tts");
        id = Snowflake.of((String) data.get("id"));
        if(data.containsKey("guild_id")) {
            guildId = Optional.of(Snowflake.of((String)data.get("guild_id")));
        } else {
            guildId = Optional.empty();
        }
        channelId = Snowflake.of((String)data.get("channel_id"));
        JSONObject a = (JSONObject) data.get("author");
        authorId = Snowflake.of((String)a.get("id"));
        author = new User(c, a);
        type = Type.valueOf((Long) data.get("type"));
        flag = Flag.valueOf((Long) data.get("flags"));
        nonce = (String) data.get("nonce");
        content = (String) data.get("content");
    }

    public Optional<Webhook> getWebhook() {
        if(!isWebhookMessage()) return Optional.empty();
        try {
            return Optional.of(new Webhook(c, (JSONObject) c.readJson(DiscordEndpoints.WEBHOOK.fulfil("webhook_id", (String) data.get("webhook_id"))).getData()));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean isWebhookMessage() {
        return data.containsKey("webhook_id");
    }

    public Optional<Member> getMember() {
        if(guildId.isEmpty()) return Optional.empty();
        if(data.containsKey("webhook_id")) return Optional.empty();
        try {
            return Optional.of(c.getDiscord().fetchGuildMember(guildId.get(), authorId));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    public Task<Channel> getChannel() {
        return Task.of(() -> {
            try {
                return c.getDiscord().fetchChannel(channelId);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
    public Optional<Guild> getGuild() {
        if(guildId.isEmpty()) return Optional.empty();
        try {
            return Optional.of(c.getDiscord().fetchGuild(guildId.get().asString()));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    public Flag getFlag() {
        return flag;
    }
    public Type getType() {
        return type;
    }
    public User getAuthor() {
        return author;
    }
    public String getNonce() {
        return nonce;
    }
    public Snowflake getId() {
        return id;
    }
    public Optional<Snowflake> getGuildId() {
        return guildId;
    }
    public Snowflake getChannelId() {
        return channelId;
    }
    public Snowflake getAuthorId() {
        return authorId;
    }
    public String getContents() {
        return content;
    }
    public boolean mentionedEveryone() {
        return mentionedEveryone;
    }
    public boolean isPinned() {
        return pinned;
    }
    public boolean isTts() {
        return tts;
    }
    /*
    {
    "op":0,
    "s":4,
    "t":"MESSAGE_CREATE",
    "d":{
        "attachments":[

        ],
        "mention_roles":[

        ],
        "edited_timestamp":null,
        "referenced_message":null,
        "mentions":[

        ],
        "member":{
            "nick":"AJ",
            "joined_at":"2020-08-06T16:51:57.683000+00:00",
            "roles":[

            ],
            "hoisted_role":null,
            "deaf":false,
            "mute":false
        },
        "embeds":[

        ],
        "timestamp":"2021-04-12T21:24:59.029000+00:00"
    }
}
     */
    public enum Type {
        DEFAULT(0),
        RECIPIENT_ADD(1),
        RECIPIENT_REMOVE(2),
        CALL(3),
        CHANNEL_NAME_CHANGE(4),
        CHANNEL_ICON_CHANGE(5),
        CHANNEL_PINNED_MESSAGE(6),
        GUILD_MEMBER_JOIN(7),
        USER_PREMIUM_GUILD_SUBSCRIPTION(8),
        USER_PREMIUM_GUILD_SUBSCRIPTION_TIER_1(9),
        USER_PREMIUM_GUILD_SUBSCRIPTION_TIER_2(10),
        USER_PREMIUM_GUILD_SUBSCRIPTION_TIER_3(11),
        CHANNEL_FOLLOW_ADD(12),
        GUILD_DISCOVERY_DISQUALIFIED(14),
        GUILD_DISCOVERY_REQUALIFIED(15),
        REPLY(19),
        APPLICATION_COMMAND(20);

        private final long id;
        Type(long id) {
            this.id = id;
        }
        public long getId() {
            return this.id;
        }
        public static Type valueOf(long id) {
            for (Type value : values()) {
                if(value.getId() == id) {
                    return value;
                }
            }
            return null;
        }
    }
    public enum Flag {
        CROSSPOSTED(1 << 0, "this message has been published to subscribed channels (via Channel Following)"),
        IS_CROSSPOST(1 << 1, "this message originated from a message in another channel (via Channel Following)"),
        SUPPRESS_EMBEDS(1 << 2, "do not include any embeds when serializing this message"),
        SOURCE_MESSAGE_DELETED(1 << 3, "the source message for this crosspost has been deleted (via Channel Following)"),
        URGENT(1 << 4, "this message came from the urgent message system");

        private final long value;
        private final String description;
        Flag(long value, String description) {
            this.value = value;
            this.description = description;
        }
        public long getValue() {
            return this.value;
        }
        public String getDescription() {
            return this.description;
        }
        public static Flag valueOf(long value) {
            for (Flag f : values()) {
                if(f.getValue() == value) {
                    return f;
                }
            }
            return null;
        }
    }
}
