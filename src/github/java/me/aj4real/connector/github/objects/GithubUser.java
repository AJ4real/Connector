package me.aj4real.connector.github.objects;

import me.aj4real.connector.Task;
import me.aj4real.connector.paginators.Paginator;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.GithubEndpoints;
import me.aj4real.connector.github.events.GithubPollingListener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class GithubUser extends GithubPerson {

    private final String name, company, type;
    private final Date createdAt, updatedAt;
    private final boolean siteAdmin;
    private static List<Long> listening = new ArrayList<>();
    public GithubUser(GithubConnector c, JSONObject data) {
        super(c, data);
        this.name = (String) data.get("name");
        this.type = (String) data.get("type");
        this.company = (String) data.get("company");
        this.siteAdmin = (Boolean) data.get("site_admin");
        this.createdAt = GithubConnector.getTimestamp((String) data.get("created_at"));
        this.updatedAt = GithubConnector.getTimestamp((String) data.get("updated_at"));
    }
    public void listen() {
        c.getHandler().listen(new GithubPollingListener(c, c.getHandler(), ((String) data.get("events_url")).replace("{/privacy}", "")));
    }
    public static Task<Optional<GithubUser>> of(GithubPerson person) {
        return Task.of(() -> {
            try {
                return Optional.of(new GithubUser(person.c, (JSONObject) person.c.readJson(GithubEndpoints.USERS.fulfil("user", person.getLoginName())).getData()));
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return Optional.empty();
            }
        });
    }
    public Paginator<List<GithubRepository>> getSubscribedRepositories() {
        return Paginator.of((i) -> {
            try {
                List<GithubRepository> repos = new ArrayList<GithubRepository>();
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.SUBSCRIBED_REPOSITORIES.fulfil("user", getLoginName()).addQuery("?per_page=100&page=" + i)).getData();
                for(Object r : arr) {
                    repos.add(new GithubRepository(c, (JSONObject) r));
                }
                return repos;
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return null;
        });
    }

    public Paginator<List<GithubRepository>> getStarredRepositories() {
        return Paginator.of((i) -> {
            try {
                List<GithubRepository> repos = new ArrayList<GithubRepository>();
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.STARRED_REPOSITORIES.fulfil("user", getLoginName()).addQuery("?per_page=100&page=" + i)).getData();
                for(Object r : arr) {
                    repos.add(new GithubRepository(c, (JSONObject) r));
                }
                return repos;
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return null;
        });
    }

    public Paginator<List<GithubPerson>> getFollowers() {
        return Paginator.of((i) -> {
            List<GithubPerson> users = new ArrayList<GithubPerson>();
            try {
                for(Object o : (JSONArray)c.readJson(GithubEndpoints.FOLLOWERS.fulfil("user", getLoginName()).addQuery("?per_page=100&page=" + i)).getData()) {
                    users.add(new GithubPerson(c, (JSONObject)o));
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
            return users;
        });
    }

    public Task<Boolean> isFollowedBy(String name) {
        return Task.of(() -> {
            try {
                return c.readJson(GithubEndpoints.FOLLOWERS.fulfil("user", getLoginName()).fulfil("other_user", name)).getResponseCode() == 204;
            } catch (Exception err) {
                err.printStackTrace();
            }
            return false;
        });
    }

    public Paginator<List<GithubPerson>> getFollowing() {
        return Paginator.of((i) -> {
            List<GithubPerson> users = new ArrayList<GithubPerson>();
            try {
                for (Object o : (JSONArray) c.readJson(GithubEndpoints.FOLLOWING.fulfil("user", getLoginName()).addQuery("?per_page=100&page=" + i)).getData()) {
                    users.add(new GithubPerson(c, (JSONObject) o));
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
            return users;
        });
    }

    public Task<Boolean> isFollowing(String name) {
        return Task.of(() -> {
            try {
                return c.readJson(GithubEndpoints.FOLLOWING.fulfil("user", getLoginName()).fulfil("other_user", name)).getResponseCode() == 204;
            } catch (Exception err) {
                err.printStackTrace();
            }
            return false;
        });
    }
    public String getType() {
        return this.type;
    }
    public String getCompanyName() {
        return this.company;
    }
    public String getName() {
        return this.name;
    }
    public Date getCreatedAt() {
        return this.createdAt;
    }
    public Date getUpdatedAt() {
        return this.updatedAt;
    }
    public boolean isSiteAdmin() {
        return this.siteAdmin;
    }

}
