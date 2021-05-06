package me.aj4real.connector.github.events.repository;

import me.aj4real.connector.Task;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.GithubEndpoints;
import me.aj4real.connector.github.events.GithubRepositoryEvent;
import me.aj4real.connector.github.objects.GithubOrganization;
import me.aj4real.connector.github.objects.GithubRepository;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Optional;

public class ForkEvent extends GithubRepositoryEvent {
    GithubRepository fork;
    public ForkEvent(GithubConnector c, JSONObject data) {
        super(c, data);
        this.fork = new GithubRepository(c, (JSONObject) ((JSONObject) data.get("payload")).get("forkee"));
    }
    public GithubRepository getFork() {
        return this.fork;
    }
    public Optional<Task<GithubOrganization>> getOrganization() {
        if (!data.containsKey("org")) return Optional.empty();
        return Optional.of(Task.of(() -> {
            try {
                return new GithubOrganization((GithubConnector) c, (JSONObject) c.readJson(GithubEndpoints.ORGANIZATIONS.fulfil("org", getOrganizationName().get())).getData());
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return null;
            }
        }));
    }
    public Optional<String> getOrganizationName() {
        if (!data.containsKey("org")) return Optional.empty();
        return Optional.of((String) ((JSONObject) data.get("org")).get("login"));
    }
    public Optional<Long> getOrganizationId() {
        if (!data.containsKey("org")) return Optional.empty();
        return Optional.of((Long) ((JSONObject) data.get("org")).get("id"));
    }
}
