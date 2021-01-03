package me.aj4real.connector.github.events.repository;

import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.events.GithubRepositoryEvent;
import org.json.simple.JSONObject;

public class GollumEvent extends GithubRepositoryEvent {
    //TODO
    public GollumEvent(GithubConnector c, JSONObject data) {
        super(c, data);
    }
}
