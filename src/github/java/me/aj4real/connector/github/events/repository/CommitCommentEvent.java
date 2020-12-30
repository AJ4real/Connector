package me.aj4real.connector.github.events.repository;

import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.events.GithubRepositoryEvent;
import me.aj4real.connector.github.objects.GitCommit;
import org.json.simple.JSONObject;

public class CommitCommentEvent extends GithubRepositoryEvent {
    private final GitCommit.Comment comment;
    public CommitCommentEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        this.comment = new GitCommit.Comment(c, (JSONObject) ((JSONObject) data.get("payload")).get("comment"));
    }
    public GitCommit.Comment getComment() {
        return this.comment;
    }
}
