package me.aj4real.connector.github.events.repository;

import me.aj4real.connector.Task;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.GithubEndpoints;
import me.aj4real.connector.github.events.GithubRepositoryEvent;
import me.aj4real.connector.github.objects.GithubUser;
import org.json.simple.JSONObject;

import java.io.IOException;

public class MemberEvent extends GithubRepositoryEvent {

    private Action action;
    public MemberEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        this.action = Action.valueOf((String) ((JSONObject)data.get("payload")).get("action"));
    }
    public Task<GithubUser> getMember() {
        return Task.of(() -> {
            try {
                return new GithubUser((GithubConnector) c, (JSONObject) c.readJson(GithubEndpoints.USERS.fulfil("user", (String) ((JSONObject) data.get("member")).get("login"))).getData());
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return null;
            }
        });
    }
    public enum Action {
        ADDED,
        EDITED;
    }
}
