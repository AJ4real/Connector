package me.aj4real.connector.github.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Mono;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.objects.GithubRepository;
import me.aj4real.connector.github.objects.GithubUser;
import org.json.simple.JSONObject;

public class PublicEvent extends GithubEvent {
    private final long actorId, repoId;
    private final String actorName, actorAvatarUrl, repoName;
    public PublicEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        JSONObject actor = (JSONObject) data.get("actor");
        this.actorId = (long) actor.get("id");
        this.actorName = (String) actor.get("login");
        this.actorAvatarUrl = (String) actor.get("avatar_url");
        JSONObject repo = (JSONObject) data.get("repo");
        this.repoName = (String) repo.get("name");
        this.repoId = (long) repo.get("id");
    }
    public Mono<GithubRepository> getRepository() {
        return Mono.of(() -> {
            try {
                return new GithubRepository((GithubConnector) c, (JSONObject) c.readJson((String) ((JSONObject)data.get("repo")).get("url")).getData());
            } catch (Exception err) {
                err.printStackTrace();
                return null;
            }
        });
    }
    public Mono<GithubUser> getActor() {
        return Mono.of(() -> {
            try {
                return new GithubUser((GithubConnector) c, (JSONObject) c.readJson((String) ((JSONObject)data.get("actor")).get("url")).getData());
            } catch (Exception err) {
                err.printStackTrace();
                return null;
            }
        });
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
    public long getRepoId() {
        return this.repoId;
    }
    public String getRepoName() {
        return this.repoName;
    }
}