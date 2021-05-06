package me.aj4real.connector.dynmap.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.events.Event;
import org.json.simple.JSONObject;

public class DynmapUpdateEvent extends Event {
    public DynmapUpdateEvent(Connector c, JSONObject data) {
        super(c, data);
    }
    public JSONObject getPayload() {
        return this.data;
    }

}
