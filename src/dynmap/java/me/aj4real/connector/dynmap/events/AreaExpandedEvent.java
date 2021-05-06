package me.aj4real.connector.dynmap.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.dynmap.objects.Area;
import me.aj4real.connector.events.Event;

public class AreaExpandedEvent extends Event {
    private final Area oldArea, newArea;
    public AreaExpandedEvent(Connector c, Area oldArea, Area newArea) {
        super(c, null);
        this.oldArea = oldArea;
        this.newArea = newArea;
    }
    public Area getOldArea() {
        return this.oldArea;
    }
    public Area getNewArea() {
        return this.newArea;
    }
}
