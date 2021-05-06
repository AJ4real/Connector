package me.aj4real.connector.dynmap.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.dynmap.objects.Area;
import me.aj4real.connector.events.Event;

public class AreaCreatedEvent extends Event {
    private final Area area;
    public AreaCreatedEvent(Connector c, Area area) {
        super(c, null);
        this.area = area;
    }
    public Area getArea() {
        return this.area;
    }
}
