package me.aj4real.connector.github.objects;

import me.aj4real.connector.Mono;
import me.aj4real.connector.paginators.Paginator;
import me.aj4real.connector.github.GithubConnector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

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
    public static class Comment {
        private final GithubConnector c;
        private final JSONObject data;
        private final long id;
        private final Optional<Long> line, position;
        private final String htmlUrl, nodeId, commitId, body;
        private final Optional<String> path;
        private final Date createdAt, updatedAt;
        private final GithubPerson user;
        public Comment(GithubConnector c, JSONObject data){
            this.c = c;
            this.data = data;
            this.id = (long) data.get("id");
            this.line = (data.get("line") != null ? Optional.of((long) data.get("line")) : Optional.empty());
            this.position = (data.get("position") != null ? Optional.of((long) data.get("position")) : Optional.empty());
            this.htmlUrl = (String) data.get("html_url");
            this.nodeId = (String) data.get("node_id");
            this.commitId = (String) data.get("commit_id");
            this.body = (String) data.get("body");
            this.path = (data.get("path") != null ? Optional.of((String) data.get("path")) : Optional.empty());
            this.createdAt = GithubConnector.getDate((String) data.get("created_at"));
            this.updatedAt = GithubConnector.getDate((String) data.get("updated_at"));
            this.user = new GithubPerson(c, (JSONObject) data.get("user"));
        }
        public Mono<GitCommit.Comment> refresh() {
            return Mono.of(() -> {
                try {
                    return new Comment(c, (JSONObject) c.readJson((String) data.get("url")).getData());
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            });
        }
        public long getId() {
            return this.id;
        }
        public Optional<Long> getLine() {
            return this.line;
        }
        public Optional<Long> getPosition() {
            return this.position;
        }
        public String getHtmlUrl() {
            return this.htmlUrl;
        }
        public String getNodeId() {
            return this.nodeId;
        }
        public String getCommitId() {
            return this.commitId;
        }
        public String getBody() {
            return this.body;
        }
        public Optional<String> getPath() {
            return this.path;
        }
        public Date getCreatedAt() {
            return createdAt;
        }
        public Date getUpdatedAt() {
            return this.updatedAt;
        }
        public GithubPerson getUser() {
            return this.user;
        }
    }
}