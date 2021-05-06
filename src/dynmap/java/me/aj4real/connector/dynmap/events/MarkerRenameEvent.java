package me.aj4real.connector.dynmap.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.dynmap.objects.Marker;
import me.aj4real.connector.events.Event;

public class MarkerRenameEvent extends Event {
    private final Marker marker;
    private final String oldName, newName;
    public MarkerRenameEvent(Connector c, Marker marker, String oldName, String newName) {
        super(c, null);
        this.marker = marker;
        this.oldName = oldName;
        this.newName = newName;
    }
    public Marker getMarker() {
        return this.marker;
    }
    public String getOldName() {
        return this.oldName;
    }
    public String getNewName() {
        return this.newName;
    }
}
