package me.aj4real.connector.discord.objects;

import me.aj4real.connector.Logger;
import me.aj4real.connector.Task;
import me.aj4real.connector.discord.DiscordConnector;
import me.aj4real.connector.discord.DiscordEndpoints;
import me.aj4real.connector.discord.RequiresDiscordPermission;
import me.aj4real.connector.discord.specs.ModifyGuildMemberSpec;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.function.Consumer;

public class Member extends User {
    private final boolean deaf, mute;
    private final String nick;
    private final Snowflake guildId;
    public Member(String guildId, DiscordConnector c, JSONObject data) {
        this(Snowflake.of(guildId), c, data);
    }
    public Member(Snowflake guildId, DiscordConnector c, JSONObject data) {
        super(c, data);
        this.guildId = guildId;
        this.deaf = (boolean) data.get("deaf");
        this.mute = (boolean) data.get("mute");
        this.nick = (String) data.get("nick");
    }
    @RequiresDiscordPermission(permission = { Role.Permission.MANAGE_NICKNAMES, Role.Permission.MANAGE_ROLES, Role.Permission.MUTE_MEMBERS, Role.Permission.DEAFEN_MEMBERS, Role.Permission.MOVE_MEMBERS})
    public Task<Member> modify(Consumer<ModifyGuildMemberSpec> spec) {
        return Task.of(() -> {
            ModifyGuildMemberSpec mutatedSpec = new ModifyGuildMemberSpec(DiscordEndpoints.MODIFY_GUILD.fulfil("guild_id", this.guildId.asString()).fulfil("user_id", this.getId().asString()));
            spec.accept(mutatedSpec);
            try {
                JSONObject member = (JSONObject) c.sendRequest(mutatedSpec).getData();
                return new Member(guildId, c, member);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
    @RequiresDiscordPermission(permission = Role.Permission.MANAGE_ROLES)
    public Task<Member> addRole(Role role) {
        return addRole(role.getId());
    }
    @RequiresDiscordPermission(permission = Role.Permission.MANAGE_ROLES)
    public Task<Member> addRole(Snowflake roleId) {
        return Task.of(() -> {
            try {
                JSONObject o = (JSONObject) c.readJson(DiscordEndpoints.ADD_GUILD_MEMBER_ROLE.fulfil("guild_id", this.guildId.asString()).fulfil("user_id", this.getId().asString()).fulfil("role_id", roleId.asString())).getData();
                return new Member(guildId, c, o);
            } catch (IOException e) {
                Logger.handle(e);
                return null;
            }
        });
    }
    @RequiresDiscordPermission(permission = Role.Permission.MANAGE_ROLES)
    public Task<Member> removeRole(Role role) {
        return removeRole(role.getId());
    }
    @RequiresDiscordPermission(permission = Role.Permission.MANAGE_ROLES)
    public Task<Member> removeRole(Snowflake roleId) {
        return Task.of(() -> {
            try {
                JSONObject o = (JSONObject) c.readJson(DiscordEndpoints.REMOVE_GUILD_MEMBER_ROLE.fulfil("guild_id", this.guildId.asString()).fulfil("user_id", this.getId().asString()).fulfil("role_id", roleId.asString())).getData();
                return new Member(guildId, c, o);
            } catch (IOException e) {
                Logger.handle(e);
                return null;
            }
        });
    }
    public boolean isDeaf() {
        return this.deaf;
    }
    public boolean isMute() {
        return this.mute;
    }
    public String getNick() {
        return this.nick;
    }
}
