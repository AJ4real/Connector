package me.aj4real.connector.dynmap.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.dynmap.objects.Area;
import me.aj4real.connector.events.Event;

public class AreaDescriptionChanged extends Event {
    private final Area area;
    private final String oldDesc, newDesc;
    public AreaDescriptionChanged(Connector c, Area area, String oldDesc, String newDesc) {
        super(c, null);
        this.area = area;
        this.oldDesc = oldDesc;
        this.newDesc = newDesc;
    }
    public Area getArea() {
        return this.area;
    }
    public String getOldDescription() {
        return this.oldDesc;
    }
    public String getNewDescription() {
        return this.newDesc;
    }
}
