package me.aj4real.connector.discord.events;

public enum Opcodes {
    DISPATCH(0, true),
    HEARTBEAT(1, true),
    IDENTIFY(2, false),
    PRESENCE_UPDATE(3, false),
    VOICE_STATE_UPDATE(4,  false),
    RESUME(6, false),
    RECONNECT(7, true),
    REQUEST_GUILD_MEMBERS(8, false),
    INVALID_SESSION(9, true),
    HELLO(10, true),
    HEARTBEAT_ACKNOWLEDGE(11, true);
    private final int code;
    private final boolean receive;
    Opcodes(int code, boolean receive) {
        this.code = code;
        this.receive = receive;
    }
}
