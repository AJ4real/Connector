package me.aj4real.connector.dynmap.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.dynmap.objects.Player;
import me.aj4real.connector.events.Event;

public class PlayerChatEvent extends Event {
    private final String message;
    private final Player player;
    public PlayerChatEvent(Connector c, Player player, String message) {
        super(c, null);
        this.message = message;
        this.player = player;
    }
    public String getMessage() {
        return message;
    }
    public Player getPlayer() {
        return player;
    }

}
