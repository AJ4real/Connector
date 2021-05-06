package me.aj4real.connector.dynmap.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.dynmap.objects.Marker;
import me.aj4real.connector.events.Event;
import org.json.simple.JSONObject;

public class MarkerCreatedEvent extends Event {
    private final Marker marker;
    public MarkerCreatedEvent(Connector c, Marker marker) {
        super(c, null);
        this.marker = marker;
    }
    public Marker getMarker() {
        return this.marker;
    }
}
