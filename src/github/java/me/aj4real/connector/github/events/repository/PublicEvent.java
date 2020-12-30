package me.aj4real.connector.github.events.repository;

import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.events.GithubRepositoryEvent;
import org.json.simple.JSONObject;

public class PublicEvent extends GithubRepositoryEvent {
    public PublicEvent(GithubConnector c, JSONObject data) {
        super(c, data);
    }
}