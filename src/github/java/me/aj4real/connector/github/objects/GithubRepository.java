package me.aj4real.connector.github.objects;

import me.aj4real.connector.Task;
import me.aj4real.connector.github.GithubApiPreviews;
import me.aj4real.connector.github.GithubEndpoints;
import me.aj4real.connector.github.events.GithubPollingListener;
import me.aj4real.connector.github.paginatorconfigurations.ListBranchesPaginatorConfiguration;
import me.aj4real.connector.github.paginatorconfigurations.ListRepositoryContributorsPaginatorConfiguration;
import me.aj4real.connector.github.paginatorconfigurations.ListRepositoryIssuesPaginatorConfiguration;
import me.aj4real.connector.github.specs.CreateIssueSpec;
import me.aj4real.connector.paginators.Paginator;
import me.aj4real.connector.Response;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.specs.ModifyRepositorySpec;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;
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
        this.createdAt = GithubConnector.getTimestamp((String) data.get("created_at"));
        this.updatedAt = GithubConnector.getTimestamp((String) data.get("updated_at"));
        this.pushedAt = GithubConnector.getTimestamp((String) data.get("pushed_at"));
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
    public Task<Branch> getBranch(String name) {
        return Task.of(() -> {
            try {
                return new Branch(c, (JSONObject) c.readJson(GithubEndpoints.LIST_REPOSITORY_BRANCHES.fulfil("owner", ownerLoginName).fulfil("repo", name).fulfil("branch", name)).getData());
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return null;
            }
        });
    }
    public Paginator<List<String>> listBranchNames() {
        return this.listBranchNames(Paginator.Configuration.DEFAULT);
    }
    public Paginator<List<String>> listBranchNames(final Consumer<? super ListBranchesPaginatorConfiguration> config) {
        return Paginator.of((i) -> {
            List<String> names = new ArrayList<>();
            try {
                ListBranchesPaginatorConfiguration mutatedConfig = new ListBranchesPaginatorConfiguration(i);
                config.accept(mutatedConfig);
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.LIST_REPOSITORY_BRANCHES.fulfil("owner", ownerLoginName).fulfil("repo", name).addQuery(mutatedConfig.buildQuery())).getData();
                for(Object o : arr) {
                    names.add((String) ((JSONObject) o).get("name"));
                }
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return names;
        });
    }
    public void listen() {
        c.getHandler().listen(new GithubPollingListener(c, c.getHandler(), ((String) data.get("events_url"))));
    }
    public Task<GithubRepository> transfer(String newOwner) {
        return Task.of(() -> {
            try {
                JSONObject o = new JSONObject();
                o.put("new_owner", newOwner);
                return new GithubRepository(c, (JSONObject) c.readJson(GithubEndpoints.TRANSFER_REPOSITORY.fulfil("owner", ownerLoginName).fulfil("repo", name), o.toString()).getData());
            } catch (Exception e) {
                me.aj4real.connector.Logger.handle(e);
                return null;
            }
        });
    }
    public Task<GithubRepository> transfer(GithubPerson p) {
        return transfer(p.getLoginName());
    }
    public Task<Boolean> isVulnerabilityAlertsEnabled() {
        return Task.of(() -> {
            try {
                return c.readJson(GithubEndpoints.REPOSITORY_VULNERABILITY_ALERTS.fulfil("owner", ownerLoginName).fulfil("repo", name)).getResponseCode() == 204;
            } catch (Exception e) {
                me.aj4real.connector.Logger.handle(e);
                return null;
            }
        });
    }
    public Task<Integer> disableVulnerabilityAlerts() {
        return Task.of(() -> {
            try {
                return c.readJson(GithubEndpoints.DISABLE_REPOSITORY_VULNERABILITY_ALERTS.fulfil("owner", ownerLoginName).fulfil("repo", name), GithubApiPreviews.ENABLE_DISABLE_VULNERABILITY_ALERTS).getResponseCode();
            } catch (Exception e) {
                me.aj4real.connector.Logger.handle(e);
                return -1;
            }
        });
    }
    public Task<Integer> enableVulnerabilityAlerts() {
        return Task.of(() -> {
            try {
                return c.readJson(GithubEndpoints.ENABLE_REPOSITORY_VULNERABILITY_ALERTS.fulfil("owner", ownerLoginName).fulfil("repo", name), GithubApiPreviews.ENABLE_DISABLE_VULNERABILITY_ALERTS).getResponseCode();
            } catch (Exception e) {
                me.aj4real.connector.Logger.handle(e);
                return -1;
            }
        });
    }
    public Paginator<List<GithubIssue>> listIssues() {
        return this.listIssues(Paginator.Configuration.DEFAULT);
    }
    public Paginator<List<GithubIssue>> listIssues(final Consumer<? super ListRepositoryIssuesPaginatorConfiguration> config) {
        return Paginator.of((i) -> {
            List<GithubIssue> commits = new ArrayList<>();
            try {
                ListRepositoryIssuesPaginatorConfiguration mutatedConfig = new ListRepositoryIssuesPaginatorConfiguration(i);
                config.accept(mutatedConfig);
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.LIST_REPOSITORY_ISSUES.fulfil("owner", ownerLoginName).fulfil("repo", name).addQuery(mutatedConfig.buildQuery())).getData();
                for(Object o : arr) {
                    commits.add(new GithubIssue(c, (JSONObject) o));
                }
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return commits;
        });
    }
    public Task<Map<String,Long>> getLanguages() {
        return Task.of(() -> {
            try {
                Map<String,Long> map = new HashMap<String,Long>();
                JSONObject o = (JSONObject) c.readJson(GithubEndpoints.LIST_REPOSITORY_LANGUAGES.fulfil("owner", ownerLoginName).fulfil("repo", name)).getData();
                for (Object k : o.keySet()) {
                    String key = (String) k;
                    if(o.get(key) instanceof Long) {
                        long value = (Long) o.get(key);
                        map.put(key, value);
                    }
                }
                return map;
            } catch (Exception e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return null;
        });
    }
    public Task<List<String>> getTopics() {
        return Task.of(() -> {
            try {
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.LIST_REPOSITORY_TOPICS.fulfil("owner", ownerLoginName).fulfil("repo", name)).getData();
                List<String> topics = new ArrayList<>();
                for (Object o1 : arr) {
                    topics.add((String) o1);
                }
                return topics;
            } catch (Exception e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return new ArrayList<>();
        });
    }
    public Paginator<List<GithubIssue.Comment>> listIssueComments() {
        return this.listIssueComments(Paginator.Configuration.DEFAULT);
    }
    public Paginator<List<GithubIssue.Comment>> listIssueComments(final Consumer<? super Paginator.Configuration> config) {
        return Paginator.of((i) -> {
            List<GithubIssue.Comment> commits = new ArrayList<>();
            try {
                Paginator.Configuration mutatedConfig = new Paginator.Configuration(i);
                config.accept(mutatedConfig);

                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.LIST_REPOSITORY_ISSUE_COMMENTS.fulfil("owner", ownerLoginName).fulfil("repo", name).addQuery(mutatedConfig.buildQuery())).getData();
                for(Object o : arr) {
                    commits.add(new GithubIssue.Comment(c, (JSONObject) o));
                }
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return commits;
        });
    }
    public Paginator<List<GitCommit>> listCommits() {
        return this.listCommits(Paginator.Configuration.DEFAULT);
    }
    public Paginator<List<GitCommit>> listCommits(final Consumer<? super Paginator.Configuration> config) {
        return Paginator.of((i) -> {
            List<GitCommit> commits = new ArrayList<>();
            try {
                Paginator.Configuration mutatedConfig = new Paginator.Configuration(i);
                config.accept(mutatedConfig);
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.LIST_REPOSITORY_COMMITS.fulfil("owner", ownerLoginName).fulfil("repo", name).addQuery(mutatedConfig.buildQuery())).getData();
                for(Object o : arr) {
                    commits.add(new GitCommit(c, (JSONObject) o));
                }
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return commits;
        });
    }
    public Paginator<List<GithubPerson>> listContributors() {
        return this.listContributors(Paginator.Configuration.DEFAULT);
    }
    public Paginator<List<GithubPerson>> listContributors(final Consumer<? super ListRepositoryContributorsPaginatorConfiguration> config) {
        return Paginator.of((i) -> {
            ListRepositoryContributorsPaginatorConfiguration mutatedConfig = new ListRepositoryContributorsPaginatorConfiguration(i);
            config.accept(mutatedConfig);
            List<GithubPerson> contributors = new ArrayList<>();
            try {
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.LIST_REPOSITORY_CONTRIBUTORS.fulfil("owner", ownerLoginName).fulfil("repo", name).addQuery(mutatedConfig.buildQuery())).getData();
                for(Object o : arr) {
                    contributors.add(new GithubPerson(c, (JSONObject) o));
                }
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return contributors;
        });
    }
    public Task<GithubPerson> getOwner() {
        return Task.of(() -> {
            try {
                return new GithubPerson(c, (JSONObject) c.readJson(GithubEndpoints.USERS.fulfil("user", ownerLoginName)).getData());
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return null;
            }
        });
    }
    public Task<GithubRepository> modify(final Consumer<? super ModifyRepositorySpec> spec) {
        return Task.of(() -> {
            ModifyRepositorySpec mutatedSpec = new ModifyRepositorySpec(GithubEndpoints.MODIFY_REPOSITORY.fulfil("owner", ownerLoginName).fulfil("repo", name));
            spec.accept(mutatedSpec);
            try {
                Response r = c.sendRequest(mutatedSpec);
                if (r.getResponseCode() == 200) {
                    return new GithubRepository(c, (JSONObject) r.getData());
                } else {
                    //TODO throw response code exception
                }
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return null;
        });
    }
    public Task<GithubIssue> createIssue(final Consumer<? super CreateIssueSpec> spec) {
        return Task.of(() -> {
            CreateIssueSpec mutatedSpec = new CreateIssueSpec(GithubEndpoints.CREATE_REPOSITORY_ISSUE.fulfil("owner", ownerLoginName).fulfil("repo", name));
            spec.accept(mutatedSpec);
            try {
                Response r = c.sendRequest(mutatedSpec);
                if (r.getResponseCode() == 201) {
                    return new GithubIssue(c, (JSONObject) r.getData());
                } else {
                    //TODO throw response code exception
                }
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return null;
        });
    }
    public Task<Integer> delete() {
        return Task.of(() -> {
            try {
                return c.readJson(GithubEndpoints.DELETE_REPOSITORY.fulfil("owner", ownerLoginName).fulfil("repo", name)).getResponseCode();
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return -1;
            }
        });
    }
    public Task<Integer> enableAutomatedSecurityFixes() {
        return Task.of(() -> {
            try {
                return c.readJson(GithubEndpoints.ENABLE_AUTOMATED_SECURITY_FIXES.fulfil("owner", ownerLoginName).fulfil("repo", name), GithubApiPreviews.ENABLE_DISABLE_AUTOMATIC_SECURITY_FIXES).getResponseCode();
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return -1;
            }
        });
    }
    public Task<Integer> disableAutomatedSecurityFixes() {
        return Task.of(() -> {
            try {
                return c.readJson(GithubEndpoints.DISABLE_AUTOMATED_SECURITY_FIXES.fulfil("owner", ownerLoginName).fulfil("repo", name), GithubApiPreviews.ENABLE_DISABLE_AUTOMATIC_SECURITY_FIXES).getResponseCode();
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return -1;
            }
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
    public String getName() {
        return this.name;
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
        private final String label, ref, sha;
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
            return new GithubPerson(c, (JSONObject) data.get("github/user"));
        }
        public GithubRepository getRepository() {
            return new GithubRepository(c, (JSONObject) data.get("github/repo"));
        }
        public static class Protection {
            protected final JSONObject data;
            protected final GithubConnector c;
            public Protection(GithubConnector c, JSONObject data) {
                this.c = c;
                this.data = data;
            }
            // TODO https://docs.github.com/en/free-pro-team@latest/rest/reference/repos#get-branch-protection
        }
    }
}
