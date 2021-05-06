package me.aj4real.connector.dynmap.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.dynmap.objects.Location;
import me.aj4real.connector.dynmap.objects.Marker;
import me.aj4real.connector.events.Event;

public class MarkerMovedEvent extends Event {
    private final Marker marker;
    private final Location oldLocation, newLocation;
    public MarkerMovedEvent(Connector c, Marker marker, Location oldLocation, Location newLocation) {
        super(c, null);
        this.marker = marker;
        this.oldLocation = oldLocation;
        this.newLocation = newLocation;
    }
}
