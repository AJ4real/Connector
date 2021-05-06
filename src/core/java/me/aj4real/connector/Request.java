package me.aj4real.connector;

import org.json.simple.JSONObject;

public abstract class Request {
    private Endpoint endpoint;
    public Request(Endpoint endpoint) {
        this.endpoint = endpoint;
    }
    public Endpoint getEndpoint() {
        return this.endpoint;
    }
    public abstract boolean isValid();
    public abstract JSONObject serialize();
}
