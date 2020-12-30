package me.aj4real.connector.github.events.repository;

import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.events.GithubRepositoryEvent;
import me.aj4real.connector.github.objects.enumerations.Action;
import org.json.simple.JSONObject;

public class WatchEvent extends GithubRepositoryEvent {
    private final Action action;
    public WatchEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        this.action = Action.valueOf(((String) ((JSONObject)data.get("payload")).get("action")).toUpperCase());
    }
}
