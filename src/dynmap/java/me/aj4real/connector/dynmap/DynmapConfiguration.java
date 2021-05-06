package me.aj4real.connector.dynmap;

import org.json.simple.JSONObject;

public class DynmapConfiguration {
    private final JSONObject data;
    public DynmapConfiguration(JSONObject config) {
        this.data = config;
    }
    public long getUpdateRefreshDelay() {
        Double d = (Double) data.get("updaterate");
        return d.longValue();
    }
}
