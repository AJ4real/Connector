package me.aj4real.connector.github.objects;

import me.aj4real.connector.Task;
import me.aj4real.connector.github.GithubEndpoints;
import me.aj4real.connector.github.paginatorconfigurations.ListRepositoriesPaginatorConfiguration;
import me.aj4real.connector.paginators.Paginator;
import me.aj4real.connector.github.GithubConnector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class GithubPerson {

    protected final JSONObject data;
    protected final GithubConnector c;
    private final String login, htmlUrl, nodeId, avatarIconUrl;
    private final long id;
    private final boolean isUser, isOrganization;

    public GithubPerson(GithubConnector c, JSONObject data) {
        this.data = data;
        this.c = c;
        this.login = (String) data.get("login");
        this.id = (long) data.get("id");
        this.htmlUrl = (String) data.get("html_url");
        this.nodeId = (String) data.get("node_id");
        this.avatarIconUrl = (String) data.get("avatar_url");
        this.isUser = ((String)this.data.get("url")).startsWith("https://api.github.com/users/");
        this.isOrganization = ((String)this.data.get("url")).startsWith("https://api.github.com/orgs/");
    }

    public Paginator<List<GithubRepository>> getRepositories() {
        return getRepositories(Paginator.Configuration.DEFAULT);
    }
    public Paginator<List<GithubRepository>> getRepositories(final Consumer<? super ListRepositoriesPaginatorConfiguration> config) {
        return Paginator.of((i) -> {
            try {
                ListRepositoriesPaginatorConfiguration mutatedConfig = new ListRepositoriesPaginatorConfiguration(i);
                config.accept(mutatedConfig);
                List<GithubRepository> orgs = new ArrayList<GithubRepository>();
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.REPOSITORY.fulfil("owner", login).addQuery(mutatedConfig.buildQuery())).getData();
                for (Object o1 : arr) {
                    orgs.add(new GithubRepository(c, (JSONObject) o1));
                }
                return orgs;
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return null;
        });
    }

    public boolean isUser() { return this.isUser; }

    public boolean isOrganization() { return this.isOrganization; }

    public Optional<Task<GithubOrganization>> toOrganization() {
        if (!isOrganization()) return Optional.empty();
        return Optional.of(Task.of(() -> {
            try {
                return new GithubOrganization(c, (JSONObject) c.readJson(GithubEndpoints.ORGANIZATIONS.fulfil("org", login)).getData());
            } catch (IOException e) {
                return null;
            }
        }));
    }

    public Optional<Task<GithubUser>> toUser() {
        if (!isUser()) return Optional.empty();
        return Optional.of(Task.of(() -> {
            try {
                return new GithubUser(c, (JSONObject) c.readJson(GithubEndpoints.USERS.fulfil("user", login)).getData());
            } catch (IOException e) {
                return null;
            }
        }));
    }

    public String getHtmlUrl() { return this.htmlUrl; }
    public String getAvatarIconUrl() {
        return this.avatarIconUrl;
    }
    public String getNodeId() {
        return this.nodeId;
    }
    public long getId() {
        return this.id;
    }
    public String getLoginName() {
        return this.login;
    }
}
