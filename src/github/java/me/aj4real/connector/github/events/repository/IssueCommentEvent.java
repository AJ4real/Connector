package me.aj4real.connector.github.events.repository;

import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.events.GithubRepositoryEvent;
import me.aj4real.connector.github.objects.GithubIssue;
import org.json.simple.JSONObject;

public class IssueCommentEvent extends GithubRepositoryEvent {
    private final Action action;
    private final GithubIssue issue;
    private final GithubIssue.Comment comment;
    public IssueCommentEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        JSONObject payload = (JSONObject) data.get("payload");
        this.action = Action.valueOf(((String) payload.get("action")).toUpperCase());
        this.issue = new GithubIssue(c, (JSONObject) payload.get("issue"));
        this.comment = new GithubIssue.Comment(c, (JSONObject) payload.get("comment"));
    }
    public Action getAction() {
        return this.action;
    }
    public GithubIssue getIssue() {
        return this.issue;
    }
    public GithubIssue.Comment getComment() {
        return this.comment;
    }
    public enum Action {
        CREATED,
        EDITED,
        DELETED;
    }
}
