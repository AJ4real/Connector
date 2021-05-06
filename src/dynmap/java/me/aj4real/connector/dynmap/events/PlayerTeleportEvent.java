package me.aj4real.connector.dynmap.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.dynmap.objects.Location;
import me.aj4real.connector.dynmap.objects.Player;
import me.aj4real.connector.events.Event;

public class PlayerTeleportEvent extends Event {
    private final Player player;
    private final Location oldLocation, newLocation;
    public PlayerTeleportEvent(Connector c, Player player, Location oldLocation, Location newLocation) {
        super(c, null);
        this.player = player;
        this.oldLocation = oldLocation;
        this.newLocation = newLocation;
    }
    public Player getPlayer() {
        return this.player;
    }
    public Location getOldLocation() {
        return this.oldLocation;
    }
    public Location getNewLocation() {
        return this.newLocation;
    }
    public double getDistance() {
        return oldLocation.between(newLocation);
    }
}
