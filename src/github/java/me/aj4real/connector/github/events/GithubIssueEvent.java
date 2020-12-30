package me.aj4real.connector.github.events;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Mono;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.objects.GitCommit;
import org.json.simple.JSONObject;

public class GithubIssueEvent extends GithubEvent {
    private final String nodeId, event, commitId;
    public GithubIssueEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        this.nodeId = (String) data.get("node_id");
        this.event = (String) data.get("event");
        this.commitId = (String) data.get("commit_id");
    }
    public Mono<GitCommit> getCommit() {
        return Mono.of(() -> {
            try {
                return new GitCommit((GithubConnector) c, (JSONObject) c.readJson((String) data.get("commit_url")).getData());
            } catch (Exception e) {
                return null;
            }
        });
    }
    public String getNodeId() {
        return nodeId;
    }
    public String getCommitId() {
        return this.commitId;
    }
    public String getEventType() {
        return this.event;
    }
}
