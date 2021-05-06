package me.aj4real.connector.github.objects;

import me.aj4real.connector.Task;
import me.aj4real.connector.Response;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.GithubEndpoints;
import me.aj4real.connector.github.events.GithubPollingListener;
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

    public Task<GithubRepository> createRepository(final Consumer<? super CreateOrganizationRepositorySpec> spec) {
        return Task.of(() -> {
            CreateOrganizationRepositorySpec mutatedSpec = new CreateOrganizationRepositorySpec(GithubEndpoints.CREATE_ORGANIZATION_REPOSITORY.fulfil("org", getLoginName()));
            spec.accept(mutatedSpec);
            try {
                Response r = c.sendRequest(mutatedSpec);
                if (r.getResponseCode() == 201) {
                    return new GithubRepository(c, (JSONObject) r.getData());
                }
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return null;
        });
    }

    public void listen() {
        c.getHandler().listen(new GithubPollingListener(c, c.getHandler(), ((String) data.get("events_url"))));
    }

    public Task<GithubOrganization> edit(final Consumer<? super ModifyOrganizationSpec> spec) {
        return Task.of(() -> {
            ModifyOrganizationSpec mutatedSpec = new ModifyOrganizationSpec(GithubEndpoints.ORGANIZATIONS.fulfil("org", getLoginName()));
            spec.accept(mutatedSpec);
            try {
                Response r = c.sendRequest(mutatedSpec);
                if (r.getResponseCode() == 201) {
                    return new GithubOrganization(c, (JSONObject) r.getData());
                }
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return null;
        });
    }

    public Task<GithubOrganization> refresh() {
        return Task.of(() -> {
            try {
                return new GithubOrganization(c, (JSONObject) c.readJson(GithubEndpoints.ORGANIZATIONS.fulfil("org", getLoginName())).getData());
            } catch (IOException e) {}
            return null;
        });
    }

    public Paginator<List<GithubPerson>> getPublicMembers() {
        return Paginator.of((i) -> {
            try {
                List<GithubPerson> members = new ArrayList<GithubPerson>();
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.ORGANIZATION_PUBLIC_MEMBERS.fulfil("org", getLoginName()).addQuery("?per_page=100&page=" + i)).getData();
                for(Object r : arr) {
                    members.add(new GithubPerson(c, (JSONObject) r));
                }
                return members;
            } catch (IOException e) {}
            return null;
        });
    }

    public Paginator<List<GithubPerson>> getMembers() {
        return Paginator.of((i) -> {
            try {
                List<GithubPerson> members = new ArrayList<GithubPerson>();
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.ORGANIZATION_MEMBERS.fulfil("org", getLoginName()).addQuery("?per_page=100&page" + i)).getData();
                for(Object r : arr) {
                    members.add(new GithubPerson(c, (JSONObject) r));
                }
                return members;
            } catch (IOException e) {}
            return null;
        });
    }
    public Task<List<GithubHook>> getHooks() {
        return Task.of(() -> {
            try {
                List<GithubHook> hooks = new ArrayList<>();
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.ORGANIZATION_HOOKS.fulfil("org", getLoginName())).getData();
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
