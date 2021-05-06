package me.aj4real.connector.discord.events;

import me.aj4real.connector.discord.events.channel.*;
import me.aj4real.connector.discord.events.guild.*;
import me.aj4real.connector.discord.events.lifecycle.*;
import me.aj4real.connector.discord.events.message.*;
import me.aj4real.connector.discord.events.interactions.ApplicationCommandCreateEvent;
import me.aj4real.connector.discord.events.interactions.ApplicationCommandDeleteEvent;
import me.aj4real.connector.discord.events.interactions.ApplicationCommandUpdateEvent;
import me.aj4real.connector.discord.events.interactions.InteractionCreateEvent;

public enum DiscordEvents {
    //TODO https://discord.com/developers/docs/topics/gateway#message-update
    HELLO(HelloEvent.class),
    READY(ReadyEvent.class),
    RESUMED(ResumedEvent.class),
    RECONNECT(ReconnectEvent.class),
    CHANNEL_CREATE(ChannelCreateEvent.class),
    CHANNEL_UDPATE(ChannelUdpateEvent.class),
    CHANNEL_DELETE(ChannelDeleteEvent.class),
    CHANNEL_PINS_UPDATE(ChannelPinsUpdateEvent.class),
    GUILD_CREATE(GuildCreateEvent.class),
    GUILD_UPDATE(GuildUpdateEvent.class),
    GUILD_DELETE(GuildDeleteEvent.class),
    GUILD_BAN_ADD(GuildBanAddEvent.class),
    GUILD_BAN_REMOVE(GuildBanRemoveEvent.class),
    GUILD_EMOJI_UPDATE(GuildEmojiUpdateEvent.class),
    GUILD_INTEGRATIONS_UPDATE(GuildIntegrationsUpdateEvent.class),
    GUILD_MEMBER_ADD(GuildMemberAddEvent.class),
    GUILD_MEMBER_REMOVE(GuildMemberRemoveEvent.class),
    GUILD_MEMBER_UPDATE(GuildMemberUpdateEvent.class),
    GUILD_MEMBER_CHUNK(GuildMemberChunkEvent.class),
    GUILD_ROLE_CREATE(GuildRoleCreateEvent.class),
    GUILD_ROLE_DELETE(GuildRoleDeleteEvent.class),
    GUILD_ROLE_UPDATE(GuildRoleUpdateEvent.class),
    INVITE_CREATE(InviteCreateEvent.class),
    INVITE_DELETE(InviteDeleteEvent.class),
    MESSAGE_CREATE(MessageCreateEvent.class),
    MESSAGE_UPDATE(MessageUpdateEvent.class),
    MESSAGE_DELETE(MessageDeleteEvent.class),
    MESSAGE_DELETE_BULK(MessageDeleteBulkEvent.class),
    MESSAGE_REACTION_ADD(MessageReactionAddEvent.class),
    MESSAGE_REACTION_REMOVE(MessageReactionRemoveEvent.class),
    MESSAGE_REACTION_REMOVE_ALL(MessageReactionRemoveAllEvent.class),
    MESSAGE_REACTION_REMOVE_EMOJI(MessageReactionRemoveEmojiEvent.class),
    PRESENCE_UPDATE(PresenceUpdateEvent.class),
    TYPING_START(TypingStartEvent.class),
    USER_UPDATE(UserUpdateEvent.class),
    VOICE_STATE_UPDATE(VoiceStateUpdateEvent.class),
    VOICE_SERVER_UPDATE(VoiceServerUpdateEvent.class),
    WEBHOOKS_UPDATE(WebhooksUpdateEvent.class),
    APPLICATION_COMMAND_CREATE(ApplicationCommandCreateEvent.class),
    APPLICATION_COMMAND_UPDATE(ApplicationCommandUpdateEvent.class),
    APPLICATION_COMMAND_DELETE(ApplicationCommandDeleteEvent.class),
    INTERACTION_CREATE(InteractionCreateEvent.class);

    private final Class clazz;

    <T extends DiscordEvent> DiscordEvents(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class getEventClass() {
        return this.clazz;
    }
}
