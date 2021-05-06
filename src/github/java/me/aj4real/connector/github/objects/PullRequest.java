package me.aj4real.connector.github.objects;

import me.aj4real.connector.Endpoint;
import me.aj4real.connector.Task;
import me.aj4real.connector.github.GithubEndpoints;
import me.aj4real.connector.paginators.Paginator;
import me.aj4real.connector.github.GithubConnector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class PullRequest {
    private final GithubConnector c;
    private final JSONObject data;
    private final Date createdAt, updatedAt;
    private final long id, number, commentsCount, reviewCommentsCount, commitsCount, additionsCount, deletionsCount, changedFilesCount;
    private final String nodeId, title, body, htmlUrl;
    private final boolean locked, draft, onlyMaintainerCanModify, merged;
    public PullRequest(GithubConnector c, JSONObject data) {
        this.c = c;
        this.data = data;
        this.createdAt = GithubConnector.getTimestamp((String) data.get("created_at"));
        this.updatedAt = GithubConnector.getTimestamp((String) data.get("updated_at"));
        this.id = (long) data.get("id");
        this.number = (long) data.get("number");
        this.commentsCount = (long) data.get("comments");
        this.reviewCommentsCount = (long) data.get("review_comments");
        this.commitsCount = (long) data.get("commits");
        this.additionsCount = (long) data.get("additions");
        this.deletionsCount = (long) data.get("deletions");
        this.changedFilesCount = (long) data.get("changed_files");
        this.nodeId = (String) data.get("node_id");
        this.title = (String) data.get("title");
        this.body = (String) data.get("body");
        this.htmlUrl = (String) data.get("html_url");
        this.locked = (boolean) data.get("locked");
        this.draft = (boolean) data.get("draft");
        this.onlyMaintainerCanModify = (boolean) data.get("maintainer_can_modify");
        this.merged = (boolean) data.get("merged");
    }
    public Date getCreatedAt() {
        return this.createdAt;
    }
    public Date getUpdatedAt() {
        return this.updatedAt;
    }
    public Optional<Date> getMergedAt() {
        if (data.get("merged_at") == null) return Optional.empty();
        return Optional.of(GithubConnector.getTimestamp((String) data.get("merged_at")));
    }
    public Optional<Date> getClosedAt() {
        if (data.get("closed_at") == null) return Optional.empty();
        return Optional.of(GithubConnector.getTimestamp((String) data.get("closed_at")));
    }
    public long getId() {
        return this.id;
    }
    public long getCommentsCount() {
        return this.commentsCount;
    }
    public long getNumber() {
        return this.number;
    }
    public long getReviewCommentsCount() {
        return this.reviewCommentsCount;
    }
    public long getCommitsCount() {
        return this.commitsCount;
    }
    public long getAdditionsCount() {
        return this.additionsCount;
    }
    public long getDeletionsCount() {
        return this.deletionsCount;
    }
    public long getChangedFilesCount() {
        return this.changedFilesCount;
    }
    public String getTitle() {
        return this.title;
    }
    public String getNodeId() {
        return this.nodeId;
    }
    public String getBody() {
        return this.body;
    }
    public String getHtmlUrl() {
        return this.htmlUrl;
    }
    public Optional<String> getMergeCommitSha() {
        if(data.get("merge_commit_sha") == null) return Optional.empty();
        return Optional.of((String) data.get("merge_commit_sha"));
    }
    public boolean isLocked() {
        return this.locked;
    }
    public boolean isDraft() {
        return this.draft;
    }
    public boolean onlyMaintainerCanModify() {
        return this.onlyMaintainerCanModify;
    }
    public boolean isMerged() {
        return this.merged;
    }
    public Optional<Boolean> isMergable() {
        if(data.get("mergable") == null) return Optional.empty();
        return Optional.of((boolean) data.get("mergable"));
    }
    public Optional<Milestone> getMilestone() {
        if(data.get("milestone") == null) return Optional.empty();
        return Optional.of(new Milestone(c, (JSONObject) data.get("milestone")));
    }
    public Task<GithubUser> getUser() {
        return Task.of(() -> {
            try {
                return new GithubUser(c, (JSONObject) c.readJson(GithubEndpoints.USERS.fulfil("user", (String) ((JSONObject) data.get("user")).get("login"))).getData());
            } catch (IOException e) {
                return null;
            }
        });
    }
    public Optional<Task<GithubUser>> getAssignee() {
        if (data.get("assignee") == null) return Optional.empty();
        return Optional.of(Task.of(() -> {
            try {
                return new GithubUser(c, (JSONObject) c.readJson(GithubEndpoints.USERS.fulfil("user", (String) ((JSONObject) data.get("assignee")).get("login"))).getData());
            } catch (IOException e) {
                return null;
            }
        }));
    }
    public List<Label> getLabels() {
        JSONArray l = (JSONArray) data.get("labels");
        List<Label> labels = new ArrayList<Label>();
        for(Object o : l) {
            labels.add(new Label(c, (JSONObject) o));
        }
        return labels;
    }
    public List<GithubPerson> getReviewers() {
        List<GithubPerson> assignees = new ArrayList<>();
        try {
            JSONArray arr = (JSONArray) data.get("requested_reviewers");
            for(Object o : arr) {
                assignees.add(new GithubPerson(c, (JSONObject) o));
            }
        } catch(Exception e) {
            me.aj4real.connector.Logger.handle(e);
        }
        return assignees;
    }
    public List<GithubPerson> getAssignees() {
        List<GithubPerson> assignees = new ArrayList<>();
        try {
            JSONArray arr = (JSONArray) data.get("assignees");
            for(Object o : arr) {
                assignees.add(new GithubPerson(c, (JSONObject) o));
            }
        } catch(Exception e) {
            me.aj4real.connector.Logger.handle(e);
        }
        return assignees;
    }

    public Paginator<List<GitCommit>> listCommits() {
        return Paginator.of((i) -> {
            List<GitCommit> commits = new ArrayList<>();
            try {
                JSONArray arr = (JSONArray) c.readJson(new Endpoint(Endpoint.HttpMethod.GET, (String) data.get("commits_url")).addQuery("?per_page=100&page=" + i)).getData();
                for(Object o : arr) {
                    commits.add(new GitCommit(c, (JSONObject) o));
                }
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return commits;
        });
    }
    public Optional<GithubIssue> getIssue() {
        if(data.get("issue_url") == null) return Optional.empty();
        try {
            return Optional.of(new GithubIssue(c, (JSONObject) c.readJson(new Endpoint(Endpoint.HttpMethod.GET, (String) data.get("issue_url"))).getData()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
    public State getState() {
        return State.valueOf(((String)data.get("state")).toUpperCase());
    }
    public GithubRepository.Branch getHead() {
        return new GithubRepository.Branch(c, (JSONObject) data.get("head"));
    }
    public GithubRepository.Branch getBase() {
        return new GithubRepository.Branch(c, (JSONObject) data.get("base"));
    }
    //TODO
    public static class Review {
        private final GithubConnector c;
        private final JSONObject data;
        private final long id;
        private final String nodeId, htmlUrl, body, commitId;
        private final State state;
        private final Date submittedAt;
        public Review(GithubConnector c, JSONObject data) {
            this.c = c;
            this.data = data;
            this.id = (long) data.get("id");
            this.nodeId = (String) data.get("node_id");
            this.htmlUrl = (String) data.get("html_url");
            this.body = (String) data.get("body");
            this.commitId = (String) data.get("commit_id");
            this.state = State.valueOf((String) data.get("state"));
            this.submittedAt = GithubConnector.getTimestamp((String) data.get("submitted_at"));
        }
        public long getId() {
            return this.id;
        }
        public String getNodeId() {
            return this.nodeId;
        }
        public String getHtmlUrl() {
            return this.htmlUrl;
        }
        public String getBody() {
            return this.body;
        }
        public String getCommitId() {
            return this.commitId;
        }
        public State getState() {
            return this.state;
        }
        public Date getSubmittedAt() {
            return this.submittedAt;
        }
        public GithubPerson getUser() {
            return new GithubPerson(c, (JSONObject) data.get("github/user"));
        }
        public Task<PullRequest> getPullRequest() {
            return Task.of(() -> {
                try {
                    return new PullRequest(c, (JSONObject) c.readJson(new Endpoint(Endpoint.HttpMethod.GET, (String) data.get("pull_request_url"))).getData());
                } catch (Exception e) {
                    me.aj4real.connector.Logger.handle(e);
                    return null;
                }
            });
        }
        public Paginator<List<Comment>> listComments() {
            return Paginator.of((i) -> {
                List<Comment> commits = new ArrayList<>();
                try {
                    JSONArray arr = (JSONArray) c.readJson(new Endpoint(Endpoint.HttpMethod.GET, ((String) data.get("pull_request_url"))+ "/reviews/" + this.id + "/comments").addQuery("?per_page=100&page=" + i)).getData();
                    for(Object o : arr) {
                        commits.add(new Comment(c, (JSONObject) o));
                    }
                } catch (IOException e) {
                    me.aj4real.connector.Logger.handle(e);
                }
                return commits;
            });
        }
        public enum State {
            APPROVE,
            REQUEST_CHANGES,
            COMMENT,
            PENDING;
        }
        public static class Comment {
            private final GithubConnector c;
            private final JSONObject data;
            private final String nodeId, diffHunk, commitId, originalCommitId, body, htmlUrl;
            private final Date createdAt, updatedAt;
            private final long id, pullRequestReviewId, repliedToId;
            public Comment(GithubConnector c, JSONObject data) {
                this.c = c;
                this.data = data;
                this.nodeId = (String) data.get("node_id");
                this.diffHunk = (String) data.get("diff_hunk");
                this.commitId = (String) data.get("commit_id");
                this.originalCommitId = (String) data.get("original_commit_id");
                this.body = (String) data.get("body");
                this.htmlUrl = (String) data.get("html_url");
                this.createdAt = GithubConnector.getTimestamp((String) data.get("created_at"));
                this.updatedAt = GithubConnector.getTimestamp((String) data.get("updated_at"));
                this.id = (long) data.get("id");
                this.pullRequestReviewId = (long) data.get("pull_request_review_id");
                this.repliedToId = (long) data.get("in_reply_to_id");
            }
            public String getNodeId() {
                return this.nodeId;
            }
            public String getBody() {
                return this.body;
            }
            public String getHtmlUrl() {
                return this.htmlUrl;
            }
            public String getCommitId() {
                return  this.commitId;
            }
            public String getDiffHunk() {
                return this.diffHunk;
            }
            public String getOriginalCommitId() {
                return this.originalCommitId;
            }
            public Date getCreatedAt() {
                return this.createdAt;
            }
            public Date getUpdatedAt() {
                return this.updatedAt;
            }
            public long getId() {
                return this.id;
            }
            public long getPullRequestReviewId() {
                return this.pullRequestReviewId;
            }
            public long getRepliedToId() {
                return this.repliedToId;
            }
            public Optional<String> getPath() {
                if (data.get("path") == null) return Optional.empty();
                return Optional.of((String) data.get("path"));
            }
            public Optional<Long> getPosition() {
                if (data.get("position") == null) return Optional.empty();
                return Optional.of((Long) data.get("position"));
            }
            public Optional<Long> getStartLine() {
                if (data.get("start_line") == null) return Optional.empty();
                return Optional.of((Long) data.get("start_line"));
            }
            public Optional<Long> getOriginalPosition() {
                if (data.get("original_position") == null) return Optional.empty();
                return Optional.of((Long) data.get("original_position"));
            }
            public Optional<Long> getOriginalStartLine() {
                if (data.get("original_start_line") == null) return Optional.empty();
                return Optional.of((Long) data.get("original_start_line"));
            }
            public Optional<Long> getLine() {
                if (data.get("line") == null) return Optional.empty();
                return Optional.of((Long) data.get("line"));
            }
            public Optional<Long> getOriginalLine() {
                if (data.get("original_line") == null) return Optional.empty();
                return Optional.of((Long) data.get("original_line"));
            }
            public Optional<Side> getSide() {
                if (data.get("side") == null) return Optional.empty();
                return Optional.of(Side.valueOf(((String) data.get("side")).toUpperCase()));
            }
            public Optional<Side> getStartSide() {
                if (data.get("start_side") == null) return Optional.empty();
                return Optional.of(Side.valueOf(((String) data.get("start_side")).toUpperCase()));
            }
            public Task<Comment> refresh() {
                return Task.of(() -> {
                    try {
                        return new Comment(c, (JSONObject) c.readJson(new Endpoint(Endpoint.HttpMethod.GET, (String) data.get("url"))).getData());
                    } catch (Exception e) {
                        me.aj4real.connector.Logger.handle(e);
                        return null;
                    }
                });
            }
            public Task<GithubUser> getUser() {
                return Task.of(() -> {
                    try {
                        return new GithubUser(c, (JSONObject) c.readJson(new Endpoint(Endpoint.HttpMethod.GET, (String) ((JSONObject) data.get("user")).get("url"))).getData());
                    } catch (Exception e) {
                        me.aj4real.connector.Logger.handle(e);
                        return null;
                    }
                });
            }
            public Task<PullRequest> getPullRequest() {
                return Task.of(() -> {
                    try {
                        return new PullRequest(c, (JSONObject) c.readJson(new Endpoint(Endpoint.HttpMethod.GET, (String) data.get("pull_request_url"))).getData());
                    } catch (Exception e) {
                        me.aj4real.connector.Logger.handle(e);
                        return null;
                    }
                });
            }
            public Task<Comment> update(String body) {
                return Task.of(() -> {
                    JSONObject o = new JSONObject();
                    o.put("body", body);
                    try {
                        JSONObject response = (JSONObject) c.readJson(new Endpoint(Endpoint.HttpMethod.PATCH, (String) data.get("url")), o.toString()).getData();
                        return new Comment(c, response);
                    } catch (IOException e) {
                        me.aj4real.connector.Logger.handle(e);
                        return null;
                    }
                });
            }
            public enum Side {
                RIGHT,
                LEFT;
            }
        }
    }
    public enum State {
        OPEN,
        CLOSED,
        ALL;
    }
}
