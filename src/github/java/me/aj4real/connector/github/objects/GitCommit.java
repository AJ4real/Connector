package me.aj4real.connector.github.objects;

import me.aj4real.connector.Paginator;
import me.aj4real.connector.github.GithubConnector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Date;

public class GitCommit {
    protected final GithubConnector c;
    protected final JSONObject data;
    private final GithubPerson committer, author;
    private final String htmlUrl, committerName, committerEmail, authorName, authorEmail, message, sha, nodeId;
    private final Date commitDate;
    private final long commentCount;
    public GitCommit(GithubConnector c, JSONObject data) {
        this.c = c;
        this.data = data;
        this.committer = new GithubPerson(c, (JSONObject) data.get("committer"));
        this.author = new GithubPerson(c, (JSONObject) data.get("author"));
        JSONObject commit = (JSONObject) data.get("commit");
        this.committerEmail = (String) ((JSONObject)commit.get("committer")).get("emails");
        this.committerName = (String) ((JSONObject)commit.get("committer")).get("name");
        this.authorEmail = (String) ((JSONObject)commit.get("author")).get("emails");
        this.authorName = (String) ((JSONObject)commit.get("author")).get("name");
        this.commitDate = GithubConnector.getDate((String) ((JSONObject)commit.get("author")).get("date"));
        this.message = (String) commit.get("message");
        this.commentCount = (long) commit.get("comment_count");
        this.sha = (String) data.get("sha");
        this.nodeId = (String) data.get("node_id");
        this.htmlUrl = (String) data.get("html_url");
    }
    public Paginator<GitCommit> getParents() {
        return Paginator.of((i) -> {
            try {
                String url = (String) (((JSONObject)((JSONArray) data.get("parents")).get(i)).get("url"));
                return new GitCommit(c, (JSONObject) c.readJson(url).getData());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
    public GithubPerson getCommitter() {
        return this.committer;
    }
    public GithubPerson getAuthor() {
        return this.author;
    }
    public Date getCommitDate() {
        return this.commitDate;
    }
    public String getCommitterName() {
        return this.committerName;
    }
    public String getCommitterEmail() {
        return this.committerEmail;
    }
    public String getAuthorName() {
        return this.authorName;
    }
    public String getAuthorEmail() {
        return this.authorEmail;
    }
    public String getMessage() {
        return this.message;
    }
    public String getSha() {
        return this.sha;
    }
    public long getCommentCount() {
        return this.commentCount;
    }
    public String getNodeId() {
        return this.nodeId;
    }
    public String getHtmlUrl() {
        return this.htmlUrl;
    }
}