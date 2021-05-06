package me.aj4real.connector.dynmap.objects;

import java.awt.*;

public class Area {
    private final String label, name, desc;
    private final Polygon polygon;
    public Area(String label, String name, String desc, Polygon polygon) {
        this.label = label;
        this.name = name;
        this.desc = desc;
        this.polygon = polygon;
    }
    public String getName() {
        return this.name;
    }
    public String getLabel() {
        return this.label;
    }
    public Polygon getPolygon() {
        return this.polygon;
    }
    public String getDescription() {
        String newDesc = "";
        boolean remove = false;
        if(desc == null) {
            return "";
        }
        char[] chars = desc.toCharArray();
        for (char c : chars) {
            if(c == '<') {
                remove = true;
            }
            if(!remove) {
                newDesc = newDesc + c;
            }
            if(c == '>') {
                remove = false;
            }
        }
        return newDesc;
    }
    public boolean compare(Area other) {
        Rectangle r = getPolygon().getBounds();
        Rectangle r2 = other.getPolygon().getBounds();
        int i = 0;
        if (r.x == r2.x) i = i++;
        if (r.y == r2.y) i = i++;
        if (r.width == r2.width) i = i++;
        if (r.height == r2.height) i = i++;
        if (i > 2) return true;
        return false;
    }
    public int getSize() {
        int[] x = getPolygon().xpoints;
        int[] z = getPolygon().ypoints;
        int hx = 0;
        int hz = 0;
        boolean first = true;
        Polygon p = new Polygon();
        for(int i = 0; i < x.length; i++) {
            if(first) {
                hx = x[i];
                hz = z[i];
            } else {
                if(hx < x[i]) hx = x[i];
                if(hz < z[i]) hz = z[i];
            }
        }
        for(int i = 0; i < x.length; i++) {
            p.addPoint(x[i] - hx, z[i] - hz);
        }
        int sum = 0;
        for (int i = 0; i < p.npoints -1; i++)
        {
            sum = sum + p.xpoints[i]*p.ypoints[i+1] - p.ypoints[i]*p.xpoints[i+1];
        }
        sum /= 2;
        return sum;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder("Area{");
        sb.append("label=" + getLabel() + ";");
        sb.append("name=" + getName() + ";");
        sb.append("description=" + getDescription() + ";");
        String s2 = "{";
        for(int i = 0; i < this.polygon.npoints; i++) {
            int x = this.polygon.xpoints[i];
            int z = this.polygon.ypoints[i];
            s2 = s2 + "[" + x + "," + z + "],";
        }
        s2 = (s2 + "}").replace(",}", "");

        sb.append("points=" + s2);
        return sb.append("}").toString();
    }
}
