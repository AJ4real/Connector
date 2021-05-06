package me.aj4real.connector.dynmap.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.dynmap.objects.Area;
import me.aj4real.connector.events.Event;

public class AreaRenamedEvent extends Event {
    private final Area area;
    private final String oldName, newName;
    public AreaRenamedEvent(Connector c, Area area, String oldName, String newName) {
        super(c, null);
        this.area = area;
        this.oldName = oldName;
        this.newName = newName;
    }
    public Area getArea() {
        return this.area;
    }
    public String getOldName() {
        return this.oldName;
    }
    public String getNewName() {
        return this.newName;
    }
}
