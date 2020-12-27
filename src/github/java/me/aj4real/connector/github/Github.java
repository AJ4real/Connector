package me.aj4real.connector.github;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Response;
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

    public Response fetchFromEndpoint(String endpoint) throws IOException {
        return c.readJson(GithubEndpoints.base + endpoint, Connector.REQUEST_METHOD.GET);
    }

    public Response fetchFromEndpoint(String endpoint, GithubApiPreviews preview) throws IOException {
        return c.readJson(GithubEndpoints.base + endpoint, Connector.REQUEST_METHOD.GET, preview);
    }

    public Optional<GithubAuthenticatedUser> fetchCurrentUser() throws IOException {
        return Optional.of(new GithubAuthenticatedUser(c, (JSONObject) c.readJson(GithubEndpoints.user, Connector.REQUEST_METHOD.GET).getData()));
    }

    public Optional<GithubUser> fetchUser(String name) throws IOException {
        return Optional.of(new GithubUser(c, (JSONObject) c.readJson(GithubEndpoints.users + "/" + name, Connector.REQUEST_METHOD.GET).getData()));
    }

    public Optional<GithubOrganization> fetchOrganization(String name) throws IOException {
        return Optional.of(new GithubOrganization(c, (JSONObject) c.readJson(GithubEndpoints.organizations + "/" + name, Connector.REQUEST_METHOD.GET).getData()));
    }

    public Optional<GithubRepository> fetchRepository(String owner, String repoName) throws IOException {

        return Optional.of(new GithubRepository(c, (JSONObject) c.readJson(GithubEndpoints.repos + "/" + owner + "/" + repoName, Connector.REQUEST_METHOD.GET).getData()));
    }
}
