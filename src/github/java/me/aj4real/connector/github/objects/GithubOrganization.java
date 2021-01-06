package me.aj4real.connector.github.objects;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Mono;
import me.aj4real.connector.Response;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.paginators.Paginator;
import me.aj4real.connector.github.specs.CreateOrganizationRepositorySpec;
import me.aj4real.connector.github.specs.ModifyOrganizationSpec;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GithubOrganization extends GithubPerson {
    private final String desc;
    public GithubOrganization(GithubConnector c, JSONObject data) {
        super(c, data);
        this.desc = (String) data.get("description");
    }

    public Mono<GithubRepository> createRepository(final Consumer<? super CreateOrganizationRepositorySpec> spec) {
        return Mono.of(() -> {
            CreateOrganizationRepositorySpec mutatedSpec = new CreateOrganizationRepositorySpec((String) data.get("repos_url"));
            spec.accept(mutatedSpec);
            try {
                Response r = c.sendRequest(mutatedSpec);
                if (r.getResponseCode() == 201) {
                    return new GithubRepository(c, (JSONObject) r.getData());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public Mono<GithubOrganization> edit(final Consumer<? super ModifyOrganizationSpec> spec) {
        return Mono.of(() -> {
            ModifyOrganizationSpec mutatedSpec = new ModifyOrganizationSpec((String) data.get("url"));
            spec.accept(mutatedSpec);
            try {
                Response r = c.sendRequest(mutatedSpec);
                if (r.getResponseCode() == 201) {
                    return new GithubOrganization(c, (JSONObject) r.getData());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public Mono<GithubOrganization> refresh() {
        return Mono.of(() -> {
            try {
                return new GithubOrganization(c, (JSONObject) c.readJson((String) data.get("url")).getData());
            } catch (IOException e) {}
            return null;
        });
    }

    public Paginator<List<GithubPerson>> getPublicMembers() {
        return Paginator.of((i) -> {
            try {
                List<GithubPerson> members = new ArrayList<GithubPerson>();
                JSONArray arr = (JSONArray) c.readJson(((String) data.get("organizations_url")).replace("{/member}", "") + "?per_page=100&page=" + i, Connector.REQUEST_METHOD.GET).getData();
                for(Object r : arr) {
                    members.add(new GithubPerson(c, (JSONObject) r));
                }
                return members;
            } catch (IOException e) {}
            return null;
        });
    }

    public Mono<List<GithubPerson>> getMembers() {
        return Mono.of(() -> {
            try {
                List<GithubPerson> members = new ArrayList<GithubPerson>();
                JSONArray arr = (JSONArray) c.readJson(((String) data.get("members_url")).replace("{/member}", "")).getData();
                for(Object r : arr) {
                    members.add(new GithubPerson(c, (JSONObject) r));
                }
                return members;
            } catch (IOException e) {}
            return null;
        });
    }
    public Mono<List<GithubHook>> getHooks() {
        return Mono.of(() -> {
            try {
                List<GithubHook> hooks = new ArrayList<>();
                JSONArray arr = (JSONArray) c.readJson((String) data.get("hooks_url")).getData();
                for (Object h : arr) {
                    hooks.add(new GithubHook(c, (JSONObject) h));
                }
                return hooks;
            } catch (IOException err) {}
            return null;
        });
    }

    public String getDescription() {
        return this.desc;
    }
}
