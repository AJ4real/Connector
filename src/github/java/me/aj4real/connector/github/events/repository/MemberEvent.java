package me.aj4real.connector.github.events.repository;

import me.aj4real.connector.Mono;
import me.aj4real.connector.github.GithubConnector;
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
    public Mono<GithubUser> getMember() {
        return Mono.of(() -> {

            try {
                return new GithubUser((GithubConnector) c, (JSONObject) c.readJson((String) ((JSONObject) data.get("member")).get("url")).getData());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
    public enum Action {
        ADDED,
        EDITED;
    }
}
