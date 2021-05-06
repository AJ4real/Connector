package me.aj4real.connector.github.objects;

import me.aj4real.connector.Endpoint;
import me.aj4real.connector.Task;
import me.aj4real.connector.Response;
import me.aj4real.connector.github.GithubEndpoints;
import me.aj4real.connector.github.specs.ModifyIssueSpec;
import me.aj4real.connector.paginators.Paginator;
import me.aj4real.connector.github.GithubConnector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class GithubIssue {
    private final GithubConnector c;
    private final JSONObject data;
    private final Date createdAt, updatedAt, closedAt;
    private final long id, number, commentsCount;
    private final String nodeId, title, message, htmlUrl, ownerLoginName, repoName;
    private final boolean locked, closed;
    public GithubIssue(GithubConnector c, JSONObject data) {
        this.c = c;
        this.data = data;
        this.id = (long) data.get("id");
        this.number = (long) data.get("number");
        this.commentsCount = (long) data.get("comments");
        this.nodeId = (String) data.get("node_id");
        this.title = (String) data.get("title");
        this.message = (String) data.get("body");
        this.htmlUrl = (String) data.get("html_url");
        JSONObject repo = (JSONObject) data.get("repository");
        this.repoName = (String) repo.get("name");
        JSONObject owner = (JSONObject) repo.get("owner");
        this.ownerLoginName = (String) owner.get("login");
        this.locked = (boolean) data.get("locked");
        this.closed = data.get("closed_by") != null;
        this.createdAt = GithubConnector.getTimestamp((String) data.get("created_at"));
        this.updatedAt = GithubConnector.getTimestamp((String) data.get("updated_at"));
        this.closedAt = GithubConnector.getTimestamp((String) data.get("closed_at"));
    }
    public void listen() {
        //TODO
        //data.get("events_url");
    }
    public long getId() {
        return this.id;
    }
    public long getIssueNumber() {
        return this.number;
    }
    public long getCommentsCount() {
        return this.commentsCount;
    }
    public String getNodeId() {
        return this.nodeId;
    }
    public String getTitle() {
        return this.title;
    }
    public String getMessage() {
        return this.message;
    }
    public String getHtmlUrl() {
        return this.htmlUrl;
    }
    public boolean isLocked() {
        return this.locked;
    }
    public boolean isClosed() {
        return this.closed;
    }
    public Date getUpdatedAt() {
        return this.updatedAt;
    }
    public Date getCreatedAt() {
        return this.createdAt;
    }
    public Date getClosedAt() {
        return this.closedAt;
    }

    public Task<GithubIssue> modify(final Consumer<? super ModifyIssueSpec> spec) {
        return Task.of(() -> {
            ModifyIssueSpec mutatedSpec = new ModifyIssueSpec(GithubEndpoints.MODIFY_REPOSITORY_ISSUE.fulfil("owner", ownerLoginName).fulfil("repo", repoName).fulfil("issue_number", String.valueOf(number)));
            spec.accept(mutatedSpec);
            try {
                Response r = c.sendRequest(mutatedSpec);
                if (r.getResponseCode() == 200) {
                    return new GithubIssue(c, (JSONObject) r.getData());
                } else {
                    //TODO throw response code exception
                }
            } catch(Exception e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return null;
        });
    }

    public Paginator<List<Comment>> listComments() {
        return Paginator.of((i) -> {
            List<Comment> comments = new ArrayList<>();
            try {
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.LIST_ISSUE_COMMENTS.fulfil("owner", ownerLoginName).fulfil("repo", repoName).fulfil("issue_number", String.valueOf(number)).addQuery("?per_page=100&page=" + i)).getData();
                for(Object o : arr) {
                    comments.add(new Comment(c, (JSONObject) o));
                }
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return comments;
        });
    }
    public Paginator<List<Label>> listLabels() {
        return Paginator.of((i) -> {
            List<Label> labels = new ArrayList<>();
            try {
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.LIST_ISSUE_LABELS.fulfil("owner", ownerLoginName).fulfil("repo", repoName).fulfil("issue_number", String.valueOf(number)).addQuery("?per_page=100&page=" + i)).getData();
                for(Object o : arr) {
                    labels.add(new Label(c, (JSONObject) o));
                }
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return labels;
        });
    }
    public Optional<LockReason> getLockReason() {
        if(data.get("active_lock_reason") == null) return Optional.empty();
        return Optional.of(LockReason.valueOf(((String) data.get("active_lock_reason")).toUpperCase().replace("-", "").replace(" ", "")));
    }
    public State getState() {
        return State.valueOf((String) data.get("state"));
    }
    public Task<GithubUser> getUser() {
        return Task.of(() -> {
            try {
                return new GithubUser(c, (JSONObject) c.readJson(GithubEndpoints.USERS.fulfil("user", (String) ((JSONObject) data.get("user")).get("url"))).getData());
            } catch (IOException e) {
                return null;
            }
        });
    }
    public Optional<Task<GithubUser>> getClosedBy() {
        if (data.get("closed_by") == null) return Optional.empty();
        return Optional.of(Task.of(() -> {
            try {
                return new GithubUser(c, (JSONObject) c.readJson(GithubEndpoints.USERS.fulfil("user", (String) ((JSONObject) data.get("closed_by")).get("url"))).getData());
            } catch (IOException e) {
                return null;
            }
        }));
    }
    public Optional<Task<GithubUser>> getAssignee() {
        if (data.get("assignee") == null) return Optional.empty();
        return Optional.of(Task.of(() -> {
            try {
                return new GithubUser(c, (JSONObject) c.readJson(GithubEndpoints.USERS.fulfil("user", (String) ((JSONObject) data.get("assignee")).get("url"))).getData());
            } catch (IOException e) {
                return null;
            }
        }));
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
    public static class Comment {
        private final GithubConnector c;
        private final JSONObject data;
        private final long id, userId;
        private final Date createdAt, updatedAt;
        private final String nodeId, message, htmlUrl, userLoginName;
        public Comment(GithubConnector c, JSONObject data) {
            this.c = c;
            this.data = data;
            this.id = (Long) data.get("id");
            this.userId = (Long) ((JSONObject) data.get("user")).get("id");
            this.createdAt = GithubConnector.getTimestamp((String) data.get("created_at"));
            this.updatedAt = GithubConnector.getTimestamp((String) data.get("updated_at"));
            this.nodeId = (String) data.get("node_id");
            this.message = (String) data.get("body");
            this.htmlUrl = (String) data.get("html_url");
            this.userLoginName = (String) ((JSONObject) data.get("user")).get("login");
        }
        public Task<GithubIssue> getIssue() {
            return Task.of(() -> {
                try {
                    return new GithubIssue(c, (JSONObject) c.readJson(new Endpoint(Endpoint.HttpMethod.GET, (String)data.get("issue_url"))).getData());
                } catch (IOException e) {
                    return null;
                }
            });
        }
        public Task<GithubUser> getUser() {
            return Task.of(() -> {
                try {
                    return new GithubUser(c, (JSONObject) c.readJson(GithubEndpoints.USERS.fulfil("user", userLoginName)).getData());
                } catch (IOException e) {
                    return null;
                }
            });
        }
        public Date getUpdatedAt() {
            return this.updatedAt;
        }
        public Date getCreatedAt() {
            return this.createdAt;
        }
        public String getNodeId() {
            return this.nodeId;
        }
        public String getMessage() {
            return this.message;
        }
        public String getHtmlUrl() {
            return this.htmlUrl;
        }
        public String getUserLoginName() {
            return this.userLoginName;
        }
        public long getId() {
            return this.id;
        }
        public long getUserId() {
            return this.userId;
        }
    }
    public enum LockReason {
        OFFTOPIC,
        TOOHEATED,
        RESOLVED,
        SPAM;
    }
    public enum Action {
        OPENED,
        CLOSED,
        REOPENED,
        ASSIGNED,
        UNASSIGNED,
        LABELED,
        UNLABELED;
    }
    public enum State {
        OPEN,
        CLOSED;
    }
}
