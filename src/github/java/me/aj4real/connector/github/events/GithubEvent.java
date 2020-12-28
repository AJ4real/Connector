package me.aj4real.connector.github.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Event;
import me.aj4real.connector.github.GithubConnector;
import org.json.simple.JSONObject;

import java.util.Date;

public class GithubEvent extends Event {
    private final long id;
    private final Date createdAt;
    private final GithubEvents type;
    private final boolean isPublic;
    public GithubEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        this.id = Long.valueOf((String) data.get("id"));
        this.createdAt = GithubConnector.getDate((String) data.get("created_at"));
        this.type = GithubEvents.valueOf((String) data.get("type"));
        this.isPublic = (boolean) data.get("public");
    }
    public long getId() {
        return this.id;
    }
    public Date getCreatedAt() {
        return this.createdAt;
    }
    public GithubEvents getType() {
        return this.type;
    }
    public boolean isPublic() {
        return this.isPublic;
    }
}
