package me.aj4real.connector.discord.objects;

import me.aj4real.connector.Logger;
import me.aj4real.connector.Task;
import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.DiscordEndpoints;
import me.aj4real.connector.discord.RequiresDiscordPermission;
import me.aj4real.connector.discord.objects.channel.GuildChannel;
import me.aj4real.connector.discord.pagInatorconfigurations.ListGuildMembersPaginatorConfiguration;
import me.aj4real.connector.discord.specs.CreateChannelSpec;
import me.aj4real.connector.discord.specs.ModifyGuildSpec;
import me.aj4real.connector.paginators.Paginator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class Guild {

    protected final DiscordConnector c;
    protected final JSONObject data;
    private final Snowflake id;
    private final String name;
    private final Snowflake ownerId;

    public Guild(DiscordConnector c, JSONObject data) {
        this.c = c;
        this.data = data;
        this.id = Snowflake.of((String) data.get("id"));
        this.name = (String) data.get("name");
        this.ownerId = Snowflake.of((String) data.get("owner_id"));
    }
    public Paginator<List<Member>> listMembers() {
        return this.listMembers((c) -> {
            c.setEntriesPerPage(1000);
        });
    }
    public Paginator<List<Member>> listMembers(Consumer<ListGuildMembersPaginatorConfiguration> config) {
        AtomicLong lastEntry = new AtomicLong(0l);
        return Paginator.of((i) -> {
            List<Member> members = new ArrayList<>();
            try {
                ListGuildMembersPaginatorConfiguration mutatedConfig = new ListGuildMembersPaginatorConfiguration(lastEntry.get());
                config.accept(mutatedConfig);
                JSONArray d = (JSONArray) c.readJson(DiscordEndpoints.GUILD_MEMBERS.fulfil("guild_id", this.id.asString()).addQuery(mutatedConfig.buildQuery())).getData();
                for (Object o : d) {
                    Member m = new Member(this.id, this.c, (JSONObject) o);
                    members.add(m);
                    lastEntry.set(m.getId().asLong());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return members;
        });
    }
    public Task<Member> getMember(Snowflake id) {
        return Task.of(() -> {
            try {
                JSONObject o = (JSONObject) c.readJson(DiscordEndpoints.GUILD_MEMBER.fulfil("guild_id", this.id.asString()).fulfil("user_id", id.asString())).getData();
                return new Member(id, c, o);
            } catch (IOException e) {
                Logger.handle(e);
                return null;
            }
        });
    }
    @RequiresDiscordPermission( permission = Role.Permission.KICK_MEMBERS )
    public Task<Void> kickMember(Snowflake id) {
        return Task.of(() -> {
            try {
                JSONObject o = (JSONObject) c.readJson(DiscordEndpoints.KICK_GUILD_MEMBER.fulfil("guild_id", this.id.asString()).fulfil("user_id", id.asString())).getData();
            } catch (IOException e) {
                Logger.handle(e);
            }
            return null;
        });
    }
    @RequiresDiscordPermission( permission = { Role.Permission.MANAGE_CHANNELS, Role.Permission.MANAGE_ROLES } )
    public Task<Channel> createChannel(final Consumer<? super CreateChannelSpec> spec) {
        return Task.of(() -> {
            CreateChannelSpec mutatedSpec = new CreateChannelSpec(DiscordEndpoints.CREATE_CHANNEL.fulfil("guild_id", this.id.asString()));
            spec.accept(mutatedSpec);
            try {
                JSONObject channel = (JSONObject) c.sendRequest(mutatedSpec).getData();
                return new Channel(c, channel);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
    @RequiresDiscordPermission( permission = Role.Permission.MANAGE_GUILD )
    public Task<Guild> modify(final Consumer<? super ModifyGuildSpec> spec) {
        return Task.of(() -> {
            ModifyGuildSpec mutatedSpec = new ModifyGuildSpec(DiscordEndpoints.MODIFY_GUILD.fulfil("guild_id", this.id.asString()));
            spec.accept(mutatedSpec);
            try {
                JSONObject guild = (JSONObject) c.sendRequest(mutatedSpec).getData();
                return new Guild(c, guild);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
    public Snowflake getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public Snowflake getOwnerId() {
        return this.ownerId;
    }
    public Optional<String> getDescription() {
        if(data.get("description") == null) return Optional.empty();
        return Optional.of((String) data.get("description"));
    }
    public <T> Task<List<? extends GuildChannel>> listChannels() {
        return Task.of(() -> {
            List<GuildChannel> channels = new ArrayList<>();
            try {
                JSONArray arr = (JSONArray) c.readJson(DiscordEndpoints.GUILD_CHANNELS.fulfil("guild_id", this.id.asString())).getData();
                for(Object o : arr) {
                    JSONObject ch = (JSONObject) o;
                    channels.add(Channel.valueOf(c, ch));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return channels;
        });
    }
    public Optional<String> getIconUrl(DiscordEndpoints.CDNFormats format) {
        if(data.get("icon") == null) return Optional.empty();
        return Optional.of(String.format(DiscordEndpoints.ICON_IMAGE_PATH, getId().asString(), data.get("icon"), format.getExtension()));
    }
    public Optional<String> getSplashUrl(DiscordEndpoints.CDNFormats format) {
        if (data.get("splash") == null) return Optional.empty();
        return Optional.of(String.format(DiscordEndpoints.SPLASH_IMAGE_PATH, getId().asString(), data.get("splash"), format.getExtension()));
    }
    public Optional<String> getDiscoverySplashUrl(DiscordEndpoints.CDNFormats format) {
        if(data.get("discovery_splash") == null) return Optional.empty();
        return Optional.of(String.format(DiscordEndpoints.DISCOVERY_SPLASH_IMAGE_PATH, getId().asString(), data.get("discovery_splashc"), format.getExtension()));
    }
    public Optional<String> getBannerUrl(DiscordEndpoints.CDNFormats format) {
        if(data.get("banner") == null) return Optional.empty();
        return Optional.of(String.format(DiscordEndpoints.BANNER_IMAGE_PATH, getId().asString(), data.get("banner"), format.getExtension()));
    }

    public List<Role> listRoles() {
        List<Role> roles = new ArrayList<>();
        for(Object o : (JSONArray) data.get("roles")) {
            roles.add(new Role(c, (JSONObject) o));
        }
        return roles;
    }
    public VoiceRegion getVoiceRegion() {
        return VoiceRegion.valueOf(((String) data.get("region")).replace("-", "_").toUpperCase());
    }

    public enum VoiceRegion {
        // https://discord.com/developers/docs/resources/voice#voice-region-object
        US_WEST("US West"),
        US_EAST("US East"),
        US_CENTRAL("US Central"),
        US_SOUTH("US South"),
        SINGAPORE("Singapore"),
        SOUTHAFRICA("South Africa"),
        SYDNEY("Sydney"),
        EUROPE("Europe"),
        BRAZIL("Brazil"),
        HONGKONG("Hong Kong"),
        RUSSIA("Russia"),
        JAPAN("Japan"),
        INDIA("India"),
        DUBAI("Dubai"),
        AMSTERDAM("Amsterdam"),
        LONDON("London"),
        FRANKFURT("Frankfurt"),
        EU_CENTRAL("Central Europe"),
        EU_WEST("Western Europe");

        private final String value;

        VoiceRegion(String value) {
            this.value = value;
        }
        public String getTitlecase() {
            return this.value;
        }
        public String getId() {
            return this.name().replace("_", "-").toLowerCase(); // standard for discord
        }
    }
}
