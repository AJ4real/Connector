package me.aj4real.connector.github.objects;

import me.aj4real.connector.Event;
import me.aj4real.connector.Mono;
import me.aj4real.connector.Paginator;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.GithubEndpoints;
import me.aj4real.connector.github.Listener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class GithubUser extends GithubPerson {

    private final String name, company, type;
    private final Date createdAt, updatedAt;
    private final boolean siteAdmin;
    public GithubUser(GithubConnector c, JSONObject data) {
        super(c, data);
        this.name = (String) data.get("name");
        this.type = (String) data.get("type");
        this.company = (String) data.get("company");
        this.siteAdmin = (Boolean) data.get("site_admin");
        this.createdAt = GithubConnector.getDate((String) data.get("created_at"));
        this.updatedAt = GithubConnector.getDate((String) data.get("updated_at"));
    }
    public <T extends Event> void listen(Class<T> eventClass, Consumer<T> consumer) {
        Listener listener = new Listener(c, ((String) data.get("events_url")).replace("{/privacy}", ""), consumer);
    }
    public static Mono<Optional<GithubUser>> of(GithubPerson person) {
        return Mono.of(() -> {
            try {
                return Optional.of(new GithubUser(person.c, (JSONObject) person.c.readJson((String) person.data.get("url")).getData()));
            } catch (IOException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        });
    }
    public Paginator<List<GithubRepository>> getSubscribedRepositories() {
        return Paginator.of((i) -> {
            try {
                List<GithubRepository> repos = new ArrayList<GithubRepository>();
                JSONArray arr = (JSONArray) c.readJson((String) data.get("subscriptions_url") + "?per_page=100&page=" + i).getData();
                for(Object r : arr) {
                    repos.add(new GithubRepository(c, (JSONObject) r));
                }
                return repos;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public Paginator<List<GithubRepository>> getStarredRepositories() {
        return Paginator.of((i) -> {
            try {
                List<GithubRepository> repos = new ArrayList<GithubRepository>();
                JSONArray arr = (JSONArray) c.readJson((String) data.get("starred_url") + "?per_page=100&page=" + i).getData();
                for(Object r : arr) {
                    repos.add(new GithubRepository(c, (JSONObject) r));
                }
                return repos;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public Paginator<List<GithubPerson>> getFollowers() {
        return Paginator.of((i) -> {
            List<GithubPerson> users = new ArrayList<GithubPerson>();
            try {
                for(Object o : (JSONArray)c.readJson(((JSONObject)data).get("followers_url").toString().replace("{/other_user}", "") + "?per_page=100&page=" + i).getData()) {
                    users.add(new GithubPerson(c, (JSONObject)o));
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
            return users;
        });
    }

    public Mono<Boolean> isFollowedBy(String name) {
        return Mono.of(() -> {
            try {
                return c.readJson(GithubEndpoints.base + "/users/" + name + "/following/" + this.getLoginName()).getResponseCode() == 204;
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
                for(Object o : (JSONArray)c.readJson(((JSONObject)data).get("following_url").toString().replace("{/other_user}", "") + "?per_page=100&page=" + i).getData()) {
                    users.add(new GithubPerson(c, (JSONObject)o));
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
            return users;
        });
    }

    public Mono<Boolean> isFollowing(String name) {
        return Mono.of(() -> {
            try {
                return c.readJson(GithubEndpoints.base + "/users/" + this.getLoginName() + "/following/" + name).getResponseCode() == 204;
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
