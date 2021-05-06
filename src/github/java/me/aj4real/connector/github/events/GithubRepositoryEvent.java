package me.aj4real.connector.github.events;

import me.aj4real.connector.Task;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.GithubEndpoints;
import me.aj4real.connector.github.objects.GithubRepository;
import org.json.simple.JSONObject;

import java.io.IOException;

public class GithubRepositoryEvent extends GithubEvent {
    private final long repoId;
    private final String repoName;
    public GithubRepositoryEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        JSONObject repo = (JSONObject) data.get("github/repo");
        this.repoName = (String) repo.get("name");
        this.repoId = (long) repo.get("id");
    }
    public long getRepoId() {
        return this.repoId;
    }
    public String getRepoName() {
        return this.repoName;
    }
    public Task<GithubRepository> getRepository() {
        return Task.of(() -> {
            try {
                return new GithubRepository((GithubConnector) c, (JSONObject) c.readJson(GithubEndpoints.REPOSITORY.fulfil("repo", repoName)).getData());
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return null;
            }
        });
    }
}
