package me.aj4real.connector.github.events.repository;

import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.events.GithubRepositoryEvent;
import org.json.simple.JSONObject;

public class CreateEvent extends GithubRepositoryEvent {
    private final String masterBranch, description, ref, refType;
    public CreateEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        JSONObject payload = (JSONObject) data.get("payload");
        this.description = (String) payload.get("description");
        this.masterBranch = (String) payload.get("master_branch");
        this.ref = (String) payload.get("ref");
        this.refType = (String) payload.get("ref_type");
    }
    public String getMasterBranch() {
        return this.masterBranch;
    }
    public String getDescription() {
        return this.description;
    }
    public String getRef() {
        return this.ref;
    }
    public String getRefType() {
        return this.refType;
    }
}
