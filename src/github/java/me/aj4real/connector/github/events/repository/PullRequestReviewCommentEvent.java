package me.aj4real.connector.github.events.repository;

import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.events.GithubRepositoryEvent;
import me.aj4real.connector.github.objects.PullRequest;
import org.json.simple.JSONObject;

public class PullRequestReviewCommentEvent extends GithubRepositoryEvent {
    PullRequest pr;
    PullRequest.Review.Comment comment;
    public PullRequestReviewCommentEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        this.pr = new PullRequest(c, (JSONObject) data.get("pull_request"));
        this.comment = new PullRequest.Review.Comment(c, (JSONObject) data.get("comment"));
    }
    public PullRequest getPullRequest() {
        return pr;
    }
    public PullRequest.Review.Comment getComment() {
        return this.comment;
    }
    //TODO
}
