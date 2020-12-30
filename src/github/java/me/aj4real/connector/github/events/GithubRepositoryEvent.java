package me.aj4real.connector.github.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Event;
import me.aj4real.connector.Mono;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.objects.GithubPerson;
import me.aj4real.connector.github.objects.GithubRepository;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Date;

public class GithubRepositoryEvent extends GithubEvent {
    private final long repoId;
    private final String repoName;
    public GithubRepositoryEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        JSONObject repo = (JSONObject) data.get("repo");
        this.repoName = (String) repo.get("name");
        this.repoId = (long) repo.get("id");
    }
    public long getRepoId() {
        return this.repoId;
    }
    public String getRepoName() {
        return this.repoName;
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
}
