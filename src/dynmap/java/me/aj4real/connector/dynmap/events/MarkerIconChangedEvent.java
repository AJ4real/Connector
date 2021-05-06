package me.aj4real.connector.dynmap.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.dynmap.objects.Marker;
import me.aj4real.connector.events.Event;
import org.json.simple.JSONObject;

public class MarkerIconChangedEvent extends Event {
    private final Marker marker;
    private final String oldIcon, newIcon;
    public MarkerIconChangedEvent(Connector c, Marker marker, String oldIcon, String newIcon) {
        super(c, null);
        this.marker = marker;
        this.oldIcon = oldIcon;
        this.newIcon = newIcon;
    }
}
