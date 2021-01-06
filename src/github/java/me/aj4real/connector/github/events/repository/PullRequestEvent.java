package me.aj4real.connector.github.events.repository;

import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.events.GithubRepositoryEvent;
import me.aj4real.connector.github.objects.PullRequest;
import org.json.simple.JSONObject;

public class PullRequestEvent extends GithubRepositoryEvent {
    private final Action action;
    private final long pullRequestNumber;
    private final PullRequest pullRequest;
    public PullRequestEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        JSONObject payload = (JSONObject) data.get("payload");
        this.action = Action.valueOf((String) payload.get("action"));
        this.pullRequestNumber = (long) payload.get("number");
        this.pullRequest = new PullRequest(c, (JSONObject) payload.get("pull_request"));
    }
    public enum Action {
        OPENED,
        CLOSED,
        REOPENED,
        ASSIGNED,
        UNASSIGNED,
        REVIEW_REQUESTED,
        REVIEW_REQUEST_REMOVED,
        LABELED,
        UNLABELED,
        SYNCHRONIZE;
    }
}
