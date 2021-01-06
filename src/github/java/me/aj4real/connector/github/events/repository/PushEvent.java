package me.aj4real.connector.github.events.repository;

import me.aj4real.connector.paginators.Paginator;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.events.GithubRepositoryEvent;
import me.aj4real.connector.github.objects.GitCommit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;

public class PushEvent extends GithubRepositoryEvent {
    private final long pushId, payloadSize;
    private final String beforeSha, headSha, ref;
    public PushEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        JSONObject payload = (JSONObject) data.get("payload");
        this.pushId = (long) payload.get("push_id");
        this.payloadSize = (long) payload.get("size");
        this.headSha = (String) payload.get("head");
        this.beforeSha = (String) payload.get("before");
        this.ref = (String) payload.get("ref");
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