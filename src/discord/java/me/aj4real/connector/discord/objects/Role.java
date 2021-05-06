package me.aj4real.connector.discord.objects;

import me.aj4real.connector.discord.DiscordConnector;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Role {
    protected final DiscordConnector c;
    protected final JSONObject data;
    private Optional<Boolean> managed = Optional.empty();
    private Optional<Boolean> hoist = Optional.empty();
    private Optional<Boolean> mentionable = Optional.empty();
    private final String name;
    private final Snowflake id;
    private final long position;
    public Role(DiscordConnector c, JSONObject data) {
        this.c = c;
        this.data = data;
        if(data.get("managed") != null) this.managed = Optional.of((boolean) data.get("managed"));
        if(data.get("hoist") != null) this.hoist = Optional.of((boolean) data.get("hoist"));
        if(data.get("mentionable") != null) this.mentionable = Optional.of((boolean) data.get("mentionable"));
        this.name = (String) data.get("name");
        this.id = Snowflake.of(String.valueOf(data.get("id")));
        this.position = (long) data.get("position");
    }
    public Optional<Boolean> isManaged() {
        return this.managed;
    }
    public Optional<Boolean> isHoist() {
        return this.hoist;
    }
    public Optional<Boolean> isMentionable() {
        return this.mentionable;
    }
    public String getName() {
        return this.name;
    }
    public Snowflake getId() {
        return this.id;
    }
    public long getPosition() {
        return this.position;
    }
    public List<Permission> getPermissions() {
        List<Permission> perms = new ArrayList<>();
        long value = Long.valueOf(String.valueOf(data.get("permissions")));
        for(Permission p : Permission.values()) {
            if((p.getValue() & value) != 0) {
                perms.add(p);
            }
        }
        return perms;
    }
    public enum Permission {
        CREATE_INSTANT_INVITE(0x00000001,"Allows creation of instant invites"),
        KICK_MEMBERS(0x00000002,"Allows kicking members"),
        BAN_MEMBERS(0x00000004,"Allows banning members"),
        ADMINISTRATOR(0x00000008,"Allows all permissions and bypasses channel permission overwrites"),
        MANAGE_CHANNELS(0x00000010,"Allows management and editing of channels"),
        MANAGE_GUILD(0x00000020,"Allows management and editing of the guild"),
        ADD_REACTIONS(0x00000040,"Allows for the addition of reactions to messages"),
        VIEW_AUDIT_LOG(0x00000080,"Allows for viewing of audit logs"),
        PRIORITY_SPEAKER(0x00000100,"Allows for using priority speaker in a voice channel"),
        STREAM(0x00000200,"Allows the user to go live"),
        VIEW_CHANNEL(0x00000400,"Allows guild members to view a channel, which includes reading messages in text channels"),
        SEND_MESSAGES(0x00000800,"Allows for sending messages in a channel"),
        SEND_TTS_MESSAGES(0x00001000,"Allows for sending of /tts messages"),
        MANAGE_MESSAGES(0x00002000,"Allows for deletion of other users messages"),
        EMBED_LINKS(0x00004000,"Links sent by users with this permission will be auto-embedded"),
        ATTACH_FILES(0x00008000,"Allows for uploading images and files"),
        READ_MESSAGE_HISTORY(0x00010000,"Allows for reading of message history"),
        MENTION_EVERYONE(0x00020000,"Allows for using the @everyone tag to notify all users in a channel, and the @here tag to notify all online users in a channel"),
        USE_EXTERNAL_EMOJIS(0x00040000,"Allows the usage of custom emojis from other servers"),
        VIEW_GUILD_INSIGHTS(0x00080000,"Allows for viewing guild insights"),
        CONNECT(0x00100000,"Allows for joining of a voice channel"),
        SPEAK(0x00200000,"Allows for speaking in a voice channel"),
        MUTE_MEMBERS(0x00400000,"Allows for muting members in a voice channel"),
        DEAFEN_MEMBERS(0x00800000,"Allows for deafening of members in a voice channel"),
        MOVE_MEMBERS(0x01000000,"Allows for moving of members between voice channels	"),
        USE_VAD(0x02000000,"Allows for using voice-activity-detection in a voice channel"),
        CHANGE_NICKNAME(0x04000000,"Allows for modification of own nickname	"),
        MANAGE_NICKNAMES(0x08000000,"Allows for modification of other users nicknames	"),
        MANAGE_ROLES(0x10000000,"Allows management and editing of roles"),
        MANAGE_WEBHOOKS (	0x20000000,"Allows management and editing of webhooks"),
        MANAGE_EMOJIS (	0x40000000,"Allows management and editing of emojis");
        private final long v;
        private final String desc;
        Permission(long v, String desc) {
            this.v = v;
            this.desc = desc;
        }
        public long getValue() {
            return this.v;
        }
        public String getDescription() {
            return this.desc;
        }
        @Override
        public String toString() {
            return name();
        }
    }
}
