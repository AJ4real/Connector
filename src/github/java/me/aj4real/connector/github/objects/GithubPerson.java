package me.aj4real.connector.github.objects;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Paginator;
import me.aj4real.connector.github.GithubConnector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        return Paginator.of((i) -> {
            try {
                List<GithubRepository> orgs = new ArrayList<GithubRepository>();
                JSONArray arr = (JSONArray) c.readJson(data.get("repos_url") + "?per_page=100&page=" + i, Connector.REQUEST_METHOD.GET).getData();
                for (Object o1 : arr) {
                    orgs.add(new GithubRepository(c, (JSONObject) o1));
                }
                return orgs;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
//
//    public boolean isUser() { return this.isUser; }
//
//    public boolean isOrganization() { return this.isOrganization; }
//
//    public Mono<GithubOrganization> getOrganization() {
//        if (!isOrganization()) return null;
//        return Mono.of(() -> {
//            try {
//                return new GithubOrganization(c, (JSONObject) c.readJson(htmlUrl).getData());
//            } catch (IOException e) {
//                return null;
//            }
//        });
//    }
//
//    public Mono<GithubUser> getUser() {
//        if (!isUser()) return null;
//        return Mono.of(() -> {
//            try {
//                return new GithubUser(c, (JSONObject) c.readJson(htmlUrl).getData());
//            } catch (IOException e) {
//                return null;
//            }
//        });
//    }

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