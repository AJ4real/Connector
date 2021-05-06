package me.aj4real.connector.dynmap.objects;

import java.awt.desktop.ScreenSleepEvent;

public class Marker {
    private final String label, name, icon, desc;
    private final Location location;
    public Marker(String label, String name, Location location, String icon, String desc) {
        this.label = label;
        this.name = name;
        this.location = location;
        this.icon = icon;
        this.desc = desc;
    }
    public String getLabel() {
        return this.label;
    }
    public String getName() {
        return this.name;
    }
    public String getIcon() {
        return this.icon;
    }
    public Location getLocation() {
        return this.location;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder("Marker{");
        sb.append("label=" + this.label + ";");
        sb.append("name=" + this.name + ";");
        sb.append("icon=" + this.icon + ";");
        sb.append("location=" + this.location.toString() + ";");
        return sb.append("}").toString();
    }
}
