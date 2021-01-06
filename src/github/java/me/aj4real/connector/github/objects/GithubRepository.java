package me.aj4real.connector.github.objects;

import me.aj4real.connector.Mono;
import me.aj4real.connector.paginators.Paginator;
import me.aj4real.connector.Response;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.specs.ModifyRepositorySpec;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class GithubRepository {
    protected final JSONObject data;
    protected final GithubConnector c;
    private final Date createdAt, updatedAt, pushedAt;
    private final String name, language, ownerLoginName, defaultBranchName, htmlLink;
    private final long stargazersCount, forksCount, id, size, watchers, openIssuesCount;
    private final boolean hasDownloads, hasProjects, hasWiki, archived, disabled, hasPages, isPrivate;
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
    public Mono<Branch> getBranch(String name) {
        return Mono.of(() -> {
            try {
                return new Branch(c, (JSONObject) c.readJson(((String) data.get("branches_url")).replace("{/branch}", "/" + name)).getData());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
    public Paginator<List<GithubIssue>> listIssues() {
        return Paginator.of((i) -> {
            List<GithubIssue> commits = new ArrayList<>();
            try {
                JSONArray arr = (JSONArray) c.readJson(((String) data.get("issues_url")).replace("{/number}", "") + "?per_page=100&page=" + i).getData();
                for(Object o : arr) {
                    commits.add(new GithubIssue(c, (JSONObject) o));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return commits;
        });
    }
    public Paginator<List<GithubIssue.Comment>> listIssueComments() {
        return Paginator.of((i) -> {
            List<GithubIssue.Comment> commits = new ArrayList<>();
            try {
                JSONArray arr = (JSONArray) c.readJson(((String) data.get("issue_comment_url")).replace("{/number}", "") + "?per_page=100&page=" + i).getData();
                for(Object o : arr) {
                    commits.add(new GithubIssue.Comment(c, (JSONObject) o));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return commits;
        });
    }
    public Paginator<List<GitCommit>> listCommits() {
        return Paginator.of((i) -> {
            List<GitCommit> commits = new ArrayList<>();
            try {
                JSONArray arr = (JSONArray) c.readJson(((String) data.get("commits_url")).replace("{/sha}", "") + "?per_page=100&page=" + i).getData();
                for(Object o : arr) {
                    commits.add(new GitCommit(c, (JSONObject) o));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return commits;
        });
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
    public static class Branch {
        protected final JSONObject data;
        protected final GithubConnector c;
        String label, ref, sha;
        public Branch(GithubConnector c, JSONObject data) {
            this.c = c;
            this.data = data;
            this.label = (String) data.get("label");
            this.ref = (String) data.get("ref");
            this.sha = (String) data.get("sha");
        }
        public String getLabel() {
            return this.label;
        }
        public String getRef() {
            return this.ref;
        }
        public String getSha() {
            return this.sha;
        }
        public GithubPerson getOwner() {
            return new GithubPerson(c, (JSONObject) data.get("user"));
        }
        public GithubRepository getRepository() {
            return new GithubRepository(c, (JSONObject) data.get("repo"));
        }
    }
}
