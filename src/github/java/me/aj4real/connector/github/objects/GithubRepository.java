package me.aj4real.connector.github.objects;

import me.aj4real.connector.Mono;
import me.aj4real.connector.Response;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.specs.ModifyRepositorySpec;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.function.Consumer;

public class GithubRepository {
    protected final JSONObject data;
    protected final GithubConnector c;
    Date createdAt, updatedAt, pushedAt;
    String name, language, ownerLoginName, defaultBranchName, htmlLink;
    long stargazersCount, forksCount, id, size, watchers, openIssuesCount;
    boolean hasDownloads, hasProjects, hasWiki, archived, disabled, hasPages, isPrivate;
    public GithubRepository(GithubConnector c, JSONObject data) {
        this.c = c;
        this.data = data;
        this.createdAt = GithubConnector.getDate((String) data.get("created_at"));
        this.updatedAt = GithubConnector.getDate((String) data.get("updated_at"));
        this.pushedAt = GithubConnector.getDate((String) data.get("pushed_at"));
        this.language = (String) data.get("language");
        this.stargazersCount = (long) data.get("stargazers_count");
        this.forksCount = (long) data.get("forks");
        this.id = (long) data.get("id");
        this.size = (long) data.get("size");
        this.watchers = (long) data.get("watchers");
        this.openIssuesCount = (long) data.get("open_issues_count");
        this.name = (String) data.get("name");
        this.ownerLoginName = (String) ((JSONObject) data.get("owner")).get("login");
        this.defaultBranchName = (String) data.get("default_branch");
        this.htmlLink = (String) data.get("html_url");
        this.hasDownloads = (boolean) data.get("has_downloads");
        this.hasProjects = (boolean) data.get("has_projects");
        this.hasWiki = (boolean) data.get("has_wiki");
        this.archived = (boolean) data.get("archived");
        this.disabled = (boolean) data.get("disabled");
        this.hasPages = (boolean) data.get("has_pages");
        this.isPrivate = (boolean) data.get("private");
    }
    public Mono<GithubUser> getOwner() {
        return Mono.of(() -> {
            try {
                return new GithubUser(c, (JSONObject) c.readJson((String) ((JSONObject) data.get("owner")).get("url")).getData());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
    public Mono<GithubRepository> modify(final Consumer<? super ModifyRepositorySpec> spec) {
        return Mono.of(() -> {
            ModifyRepositorySpec mutatedSpec = new ModifyRepositorySpec((String) data.get("url"));
            spec.accept(mutatedSpec);
            try {
                Response r = c.sendRequest(mutatedSpec);
                if (r.getResponseCode() == 200) {
                    return new GithubRepository(c, (JSONObject) r.getData());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
    public Date getUpdatedAt() {
        return this.updatedAt;
    }
    public Date getCreatedAt() {
        return this.createdAt;
    }
    public Date getPushedAt() {
        return this.pushedAt;
    }
    public long getStargazersCount() {
        return this.stargazersCount;
    }
    public long getForksCount() {
        return this.forksCount;
    }
    public long getId() {
        return this.id;
    }
    public long getSize() {
        return this.size;
    }
    public long getWatchersCount() {
        return this.watchers;
    }
    public long getOpenIssuesCount() {
        return this.openIssuesCount;
    }
    public String getLanguage() {
        return this.language;
    }
    public String getDefaultBranchName() {
        return this.defaultBranchName;
    }
    public String getHtmlLink() {
        return this.htmlLink;
    }
    public String getOwnerLoginName() {
        return ownerLoginName;
    }
    public boolean hasDownloads() {
        return this.hasDownloads;
    }
    public boolean hasPages() {
        return this.hasPages;
    }
    public boolean hasProjects() {
        return this.hasProjects;
    }
    public boolean hasWiki() {
        return this.hasWiki;
    }
    public boolean isArchived() {
        return this.archived;
    }
    public boolean isDisabled() {
        return this.disabled;
    }
    public boolean isPrivate() {
        return this.isPrivate;
    }
}
