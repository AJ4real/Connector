package me.aj4real.connector.github.objects;

import me.aj4real.connector.Mono;
import me.aj4real.connector.Paginator;
import me.aj4real.connector.github.GithubConnector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class GithubIssue {
    private final GithubConnector c;
    private final JSONObject data;
    private final Date createdAt, updatedAt, closedAt;
    private final long id, number, commentsCount;
    private final String nodeId, title, message, htmlUrl;
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
        this.locked = (boolean) data.get("locked");
        this.closed = data.get("closed_by") != null;
        this.createdAt = GithubConnector.getDate((String) data.get("created_at"));
        this.updatedAt = GithubConnector.getDate((String) data.get("updated_at"));
        this.closedAt = GithubConnector.getDate((String) data.get("closed_at"));
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
    public Paginator<List<Comment>> listComments() {
        return Paginator.of((i) -> {
            List<Comment> comments = new ArrayList<>();
            try {
                JSONArray arr = (JSONArray) c.readJson(((String) data.get("comments_url")) + "?per_page=100&page=" + i).getData();
                for(Object o : arr) {
                    comments.add(new Comment(c, (JSONObject) o));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return comments;
        });
    }
    public Paginator<List<Label>> listLabels() {
        return Paginator.of((i) -> {
            List<Label> labels = new ArrayList<>();
            try {
                JSONArray arr = (JSONArray) c.readJson(((String) data.get("labels_url")).replace("{/name}", "") + "?per_page=100&page=" + i).getData();
                for(Object o : arr) {
                    labels.add(new Label(c, (JSONObject) o));
                }
            } catch (IOException e) {
                e.printStackTrace();
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
    public Mono<GithubUser> getUser() {
        return Mono.of(() -> {
            try {
                return new GithubUser(c, (JSONObject) c.readJson((String) ((JSONObject) data.get("user")).get("url")).getData());
            } catch (IOException e) {
                return null;
            }
        });
    }
    public Optional<Mono<GithubUser>> getClosedBy() {
        if (data.get("closed_by") == null) return Optional.empty();
        return Optional.of(Mono.of(() -> {
            try {
                return new GithubUser(c, (JSONObject) c.readJson((String) ((JSONObject) data.get("closed_by")).get("url")).getData());
            } catch (IOException e) {
                return null;
            }
        }));
    }
    public Optional<Mono<GithubUser>> getAssignee() {
        if (data.get("assignee") == null) return Optional.empty();
        return Optional.of(Mono.of(() -> {
            try {
                return new GithubUser(c, (JSONObject) c.readJson((String) ((JSONObject) data.get("assignee")).get("url")).getData());
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
            e.printStackTrace();
        }
        return assignees;
    }
    public static class Comment {
        private final GithubConnector c;
        private final JSONObject data;
        private final long id;
        private final Date createdAt, updatedAt;
        private final String nodeId, message, htmlUrl;
        public Comment(GithubConnector c, JSONObject data) {
            this.c = c;
            this.data = data;
            this.id = (Integer) data.get("id");
            this.createdAt = GithubConnector.getDate((String) data.get("created_at"));
            this.updatedAt = GithubConnector.getDate((String) data.get("updated_at"));
            this.nodeId = (String) data.get("node_id");
            this.message = (String) data.get("body");
            this.htmlUrl = (String) data.get("html_url");
        }
        public Mono<GithubIssue> getIssue() {
            return Mono.of(() -> {
                try {
                    return new GithubIssue(c, (JSONObject) c.readJson((String)data.get("issue_url")).getData());
                } catch (IOException e) {
                    return null;
                }
            });
        }
        public Mono<GithubUser> getUser() {
            return Mono.of(() -> {
                try {
                    return new GithubUser(c, (JSONObject) c.readJson((String) ((JSONObject) data.get("user")).get("url")).getData());
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
    }
    public static class Label {
        private final GithubConnector c;
        private final JSONObject data;
        private final long id;
        private final String nodeId, name, description, hexColor;
        private final boolean isDefault;
        public Label(GithubConnector c, JSONObject data) {
            this.c = c;
            this.data = data;
            this.id = (long) data.get("id");
            this.nodeId = (String) data.get("node_id");
            this.name = (String) data.get("name");
            this.description = (String) data.get("description");
            this.hexColor = (String) data.get("color");
            this.isDefault = (boolean) data.get("default");
        }
        public long getId() {
            return this.id;
        }
        public String getNodeId() {
            return this.nodeId;
        }
        public String getDescription() {
            return this.description;
        }
        public String getName() {
            return this.name;
        }
        public String getHexColor() {
            return this.hexColor;
        }
        public boolean isDefault() {
            return this.isDefault;
        }
    }
    public enum LockReason {
        OFFTOPIC,
        TOOHEATED,
        RESOLVED,
        SPAM;
    }
    public enum State {
        OPEN,
        CLOSED;
    }
}
