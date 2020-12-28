package me.aj4real.connector;

import org.json.simple.JSONObject;

public class Event {
    protected final Connector c;
    protected final JSONObject data;
    public Event(Connector c, JSONObject data) {
        System.out.println(data);
        this.c = c;
        this.data = data;
    }
}
