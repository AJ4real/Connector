package me.aj4real.connector.github;

import me.aj4real.connector.github.objects.GithubAuthenticatedUser;
import me.aj4real.connector.github.objects.GithubOrganization;
import me.aj4real.connector.github.objects.GithubRepository;
import me.aj4real.connector.github.objects.GithubUser;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Optional;

public class Github {
    GithubConnector c;
    public Github(GithubConnector c) {
        this.c = c;
    }

    public Optional<GithubAuthenticatedUser> fetchCurrentUser() throws IOException {
        return Optional.of(new GithubAuthenticatedUser(c, (JSONObject) c.readJson(GithubEndpoints.AUTHENTICATED_USER).getData()));
    }

    public Optional<GithubUser> fetchUser(String name) throws IOException {
        return Optional.of(new GithubUser(c, (JSONObject) c.readJson(GithubEndpoints.USERS.fulfil("user", name)).getData()));
    }

    public Optional<GithubOrganization> fetchOrganization(String name) throws IOException {
        return Optional.of(new GithubOrganization(c, (JSONObject) c.readJson(GithubEndpoints.ORGANIZATIONS.fulfil("org", name)).getData()));
    }

    public Optional<GithubRepository> fetchRepository(String owner, String repoName) throws IOException {
        return Optional.of(new GithubRepository(c, (JSONObject) c.readJson(GithubEndpoints.REPOSITORY.fulfil("owner", owner).fulfil("repo", repoName)).getData()));
    }
}
