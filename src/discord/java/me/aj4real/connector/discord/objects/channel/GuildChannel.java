package me.aj4real.connector.discord.objects.channel;

import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.objects.Snowflake;
import me.aj4real.connector.discord.objects.Channel;
import org.json.simple.JSONObject;

import java.util.Optional;

public class GuildChannel extends Channel {
    private Snowflake guildId;
    private String name;
    private long position;
    private boolean nsfw;
    public GuildChannel(DiscordConnector c, JSONObject data) {
        super(c, data);
        this.guildId = Snowflake.of((String) data.get("guild_id"));
        this.name = (String) data.get("name");
        this.position = (Long) data.get("position");
        this.nsfw = (Boolean) data.get("nsfw");
    }
    public String getName() {
        return this.name;
    }
    public Snowflake getGuildId() {
        return this.guildId;
    }
    public long getPosition() {
        return this.position;
    }
    public boolean isNsfw() {
        return this.nsfw;
    }
    public Optional<Snowflake> getParentId() {
        if(data.get("parent_id") == null) return Optional.empty();
        return Optional.of(Snowflake.of((String) data.get("parent_id")));
    }
//    public List<> getPermissionOverrides() {
//        JSONArray arr = data.get("permission_overwrites");
//        //TODO
//    }
}
