package me.aj4real.connector.github.events;

import me.aj4real.connector.Mono;
import me.aj4real.connector.Paginator;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.objects.GitCommit;
import me.aj4real.connector.github.objects.GithubPerson;
import me.aj4real.connector.github.objects.GithubRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;

public class PushEvent extends GithubEvent {
    private final long actorId, repoId, pushId, payloadSize;
    private final String actorName, actorAvatarUrl, repoName, beforeSha, headSha, ref;
    public PushEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        JSONObject actor = (JSONObject) data.get("actor");
        this.actorId = (long) actor.get("id");
        this.actorName = (String) actor.get("login");
        this.actorAvatarUrl = (String) actor.get("avatar_url");
        JSONObject repo = (JSONObject) data.get("repo");
        this.repoName = (String) repo.get("name");
        this.repoId = (long) repo.get("id");
        JSONObject payload = (JSONObject) data.get("payload");
        this.pushId = (long) payload.get("push_id");
        this.payloadSize = (long) payload.get("size");
        this.headSha = (String) payload.get("head");
        this.beforeSha = (String) payload.get("before");
        this.ref = (String) payload.get("ref");
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
    public long getPushId() {
        return this.pushId;
    }
    public long getPayloadSize() {
        return this.payloadSize;
    }
    public String getBeforeSha() {
        return this.beforeSha;
    }
    public String getHeadSha() {
        return this.headSha;
    }
    public String getRef() {
        return this.ref;
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
    public Mono<GithubRepository> getRepository() {
        return Mono.of(() -> {
            try {
                return new GithubRepository((GithubConnector) c, (JSONObject) c.readJson((String) ((JSONObject)data.get("repo")).get("url")).getData());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
    public Paginator<GitCommit> getCommits() {
        return Paginator.of((i) -> {
            JSONArray commitsRaw = (JSONArray)((JSONObject) data.get("payload")).get("commits");
            try {
                return new GitCommit((GithubConnector) c, (JSONObject) c.readJson((String) ((JSONObject)commitsRaw.get(i)).get("url")).getData());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
}