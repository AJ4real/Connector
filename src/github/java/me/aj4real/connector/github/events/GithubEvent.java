package me.aj4real.connector.github.events;

import me.aj4real.connector.events.Event;
import me.aj4real.connector.Mono;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.objects.GithubPerson;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Date;

public class GithubEvent extends Event {
    private final long id, actorId;
    private final Date createdAt;
    private final GithubRepositoryEvents type;
    private final boolean isPublic;
    private final String actorName, actorAvatarUrl;
    public GithubEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        this.id = Long.valueOf((String) data.get("id"));
        JSONObject actor = (JSONObject) data.get("actor");
        this.actorId = (long) actor.get("id");
        this.actorName = (String) actor.get("login");
        this.actorAvatarUrl = (String) actor.get("avatar_url");
        this.createdAt = GithubConnector.getTimestamp((String) data.get("created_at"));
        this.type = GithubRepositoryEvents.valueOf((String) data.get("type"));
        this.isPublic = (boolean) data.get("public");
    }
    public Date getCreatedAt() {
        return this.createdAt;
    }
    public GithubRepositoryEvents getType() {
        return this.type;
    }
    public boolean isEventPublic() {
        return this.isPublic;
    }
    public long getId() {
        return this.id;
    }
    public long getActorId() {
        return this.actorId;
    }
    public String getActorName() {
        return actorName;
    }
    public String getActorAvatarUrl() {
        return this.actorAvatarUrl;
    }
    public Mono<GithubPerson> getActor() {
        return Mono.of(() -> {
            try {
                return new GithubPerson((GithubConnector) c, (JSONObject) c.readJson((String) ((JSONObject)data.get("actor")).get("url")).getData());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
}
