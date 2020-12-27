package me.aj4real.connector.github.objects;

import me.aj4real.connector.github.GithubConnector;
import org.json.simple.JSONObject;

public class GithubRepositoryBranch extends GithubRepository {
    public GithubRepositoryBranch(GithubConnector c, JSONObject data, GithubRepository repo) {
        super(c, repo.data);
    }
}
