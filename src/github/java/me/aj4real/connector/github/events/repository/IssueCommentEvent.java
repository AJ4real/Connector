package me.aj4real.connector.github.events.repository;

import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.events.GithubRepositoryEvent;
import me.aj4real.connector.github.objects.GithubIssue;
import me.aj4real.connector.github.objects.enumerations.Action;
import org.json.simple.JSONObject;

public class IssueCommentEvent extends GithubRepositoryEvent {
    Action action;
    GithubIssue issue;
    GithubIssue.Comment comment;
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
}
