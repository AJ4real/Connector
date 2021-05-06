package me.aj4real.connector.dynmap.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.dynmap.objects.Area;
import me.aj4real.connector.dynmap.objects.Player;
import me.aj4real.connector.events.Event;

public class PlayerEntersAreaEvent extends Event {
    private final Player player;
    private final Area area;
    private final boolean visible;
    public PlayerEntersAreaEvent(Connector c, Player player, Area area, boolean visible) {
        super(c, null);
        this.player = player;
        this.area = area;
        this.visible = visible;
    }
    public Player getPlayer() {
        return this.player;
    }
    public boolean appeared() {
        return this.visible;
    }
    public Area getArea() {
        return this.area;
    }
}
