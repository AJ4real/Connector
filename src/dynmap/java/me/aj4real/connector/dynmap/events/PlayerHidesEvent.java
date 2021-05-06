package me.aj4real.connector.dynmap.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.dynmap.objects.Player;
import me.aj4real.connector.events.Event;

public class PlayerHidesEvent extends Event {
    private final Player player;
    public PlayerHidesEvent(Connector c, Player player) {
        super(c, null);
        this.player = player;
    }
    public Player getPlayer() {
        return this.player;
    }
}
