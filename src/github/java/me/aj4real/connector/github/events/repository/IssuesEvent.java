package me.aj4real.connector.github.events.repository;

import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.events.GithubRepositoryEvent;
import me.aj4real.connector.github.objects.GithubIssue;
import org.json.simple.JSONObject;

public class IssuesEvent extends GithubRepositoryEvent {
    private final GithubIssue issue;
    private final GithubIssue.Action action;
    public IssuesEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        this.issue = new GithubIssue(c, (JSONObject) ((JSONObject) data.get("payload")).get("issue"));
        this.action = GithubIssue.Action.valueOf(((String) ((JSONObject) data.get("payload")).get("action")).toUpperCase());
    }
    public GithubIssue getIssue() {
        return this.issue;
    }
    public GithubIssue.Action getAction() {
        return this.action;
    }
}
