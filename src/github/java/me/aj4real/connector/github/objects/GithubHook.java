package me.aj4real.connector.github.objects;

import me.aj4real.connector.Mono;
import me.aj4real.connector.github.GithubConnector;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Date;

public class GithubHook {
    protected final JSONObject data;
    protected final GithubConnector c;
    private final Date createdAt, updatedAt;
    private final boolean isActive;
    private final String contextType, url;
    private final long id;
    public GithubHook(GithubConnector c, JSONObject data) {
        this.data = data;
        this.c = c;
        this.updatedAt = GithubConnector.getTimestamp((String) data.get("updated_at"));
        this.createdAt = GithubConnector.getTimestamp((String) data.get("created_at"));
        this.isActive = (boolean) data.get("active");
        this.contextType = (String) ((JSONObject)data.get("config")).get("content_type");
        this.url = (String) ((JSONObject)data.get("config")).get("url");
        this.id = (long) data.get("id");
    }

    public Mono<GithubHook> refresh() {
        return Mono.of(() -> {
            try {
                return new GithubHook(c, (JSONObject) c.readJson((String) data.get("url")).getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
    public String getContextType() {
        return this.contextType;
    }
    public boolean isActive() {
        return this.isActive;
    }
    public String getUrl() {
        return this.url;
    }
    public Date getCreatedAt() {
        return this.createdAt;
    }
    public Date getUpdatedAt() {
        return this.updatedAt;
    }
}
