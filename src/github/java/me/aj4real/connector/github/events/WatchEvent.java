package me.aj4real.connector.github.events;

import me.aj4real.connector.github.GithubConnector;
import org.json.simple.JSONObject;

public class WatchEvent extends GithubEvent {
    public WatchEvent(GithubConnector c, JSONObject data) {
        super(c, data);
    }
}
