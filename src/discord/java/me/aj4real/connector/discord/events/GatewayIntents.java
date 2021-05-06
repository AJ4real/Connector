package me.aj4real.connector.discord.events;

import java.util.Collection;
import java.util.EnumSet;

public enum GatewayIntents {
    GUILD_MEMBERS(1),
    GUILD_BANS(2),
    GUILD_EMOJIS(3),
    GUILD_INTEGRATIONS(4),
    GUILD_WEBHOOKS(5),
    GUILD_INVITES(6),
    GUILD_VOICE_STATES(7),
    GUILD_PRESENCES(8),
    GUILD_MESSAGES(9),
    GUILD_MESSAGE_REACTIONS(10),
    GUILD_MESSAGE_TYPING(11),
    DIRECT_MESSAGES(12),
    DIRECT_MESSAGE_REACTIONS(13),
    DIRECT_MESSAGE_TYPING(14);

    public static final int ALL_INTENTS = 1 | getRaw(EnumSet.allOf(GatewayIntents.class));

    private final int value;
    private final int offset;

    GatewayIntents(int offset)
    {
        this.offset = offset;
        this.value = 1 << offset;
    }

    public int getValue()
    {
        return value;
    }

    public int getOffset()
    {
        return offset;
    }

    public static int getRaw(Collection<GatewayIntents> set)
    {
        int raw = 0;
        for (GatewayIntents intent : set)
            raw |= intent.value;
        return raw;
    }

}
