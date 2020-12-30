package me.aj4real.connector.github.events.repository;

import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.events.GithubRepositoryEvent;
import org.json.simple.JSONObject;

public class DeleteEvent extends GithubRepositoryEvent {
    private final String ref, refType;
    public DeleteEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        JSONObject payload = (JSONObject) data.get("payload");
        this.ref = (String) payload.get("ref");
        this.refType = (String) payload.get("ref_type");
    }
    public String getRef() {
        return this.ref;
    }
    public String getRefType() {
        return this.refType;
    }
}
