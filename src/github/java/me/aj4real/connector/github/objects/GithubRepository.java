package me.aj4real.connector.github.objects;

import me.aj4real.connector.Mono;
import me.aj4real.connector.Response;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.specs.ModifyRepositorySpec;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.function.Consumer;

public class GithubRepository {
    protected final JSONObject data;
    protected final GithubConnector c;
    Date createdAt, updatedAt, pushedAt;
    String name, language;
    long stargazersCount, forksCount, id, size;
    public GithubRepository(GithubConnector c, JSONObject data) {
        this.c = c;
        this.data = data;
        this.createdAt = GithubConnector.getDate((String) data.get("created_at"));
        this.updatedAt = GithubConnector.getDate((String) data.get("updated_at"));
        this.pushedAt = GithubConnector.getDate((String) data.get("pushed_at"));
        this.language = (String) data.get("language");
        this.stargazersCount = (long) data.get("stargazers_count");
        this.forksCount = (long) data.get("forks");
        this.id = (long) data.get("id");
        this.size = (long) data.get("size");
        this.name = (String) data.get("name");
    }
    public Mono<GithubRepository> modify(final Consumer<? super ModifyRepositorySpec> spec) {
        return Mono.of(() -> {
            ModifyRepositorySpec mutatedSpec = new ModifyRepositorySpec((String) data.get("url"));
            spec.accept(mutatedSpec);
            try {
                Response r = c.sendRequest(mutatedSpec);
                if (r.getResponseCode() == 200) {
                    return new GithubRepository(c, (JSONObject) r.getData());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
