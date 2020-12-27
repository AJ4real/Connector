package me.aj4real.connector;

import org.json.simple.JSONObject;

public abstract class Request {
    private String url;
    public Request(String url) {
        this.url = url;
    }
    public String getUrl() {
        return this.url;
    }
    public abstract Connector.REQUEST_METHOD getRequestMethod();
    public abstract boolean isValid();
    public abstract JSONObject serialize();
}
