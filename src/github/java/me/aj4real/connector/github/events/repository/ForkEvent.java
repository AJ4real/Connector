package me.aj4real.connector.github.events.repository;

import me.aj4real.connector.Mono;
import me.aj4real.connector.github.GithubConnector;
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
    public Optional<Mono<GithubOrganization>> getOrganization() {
        if (!data.containsKey("org")) return Optional.empty();
        return Optional.of(Mono.of(() -> {
            try {
                return new GithubOrganization((GithubConnector) c, (JSONObject) c.readJson((String) ((JSONObject) data.get("org")).get("url")).getData());
            } catch (IOException e) {
                return null;
            }
        }));
    }
}
