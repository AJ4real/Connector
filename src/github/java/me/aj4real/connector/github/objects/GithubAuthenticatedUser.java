package me.aj4real.connector.github.objects;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Mono;
import me.aj4real.connector.Response;
import me.aj4real.connector.github.GithubApiPreviews;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.GithubEndpoints;
import me.aj4real.connector.Paginator;
import me.aj4real.connector.github.specs.CreateRepositorySpec;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class GithubAuthenticatedUser extends GithubUser {
    private final long followersCount;
    private final long followingCount;
    private final long publicReposCount;
    private final long publicGistsCount;
    public GithubAuthenticatedUser(GithubConnector c, JSONObject data) {
        super(c, data);
        this.followingCount = (long) data.get("following");
        this.followersCount = (long) data.get("followers");
        this.publicGistsCount = (long) data.get("public_gists");
        this.publicReposCount = (long) data.get("public_repos");
    }

    public Paginator<List<GithubOrganization>> getOrganizations() {
        return Paginator.of((i) -> {
            try {
                List<GithubOrganization> orgs = new ArrayList<GithubOrganization>();
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.user + "/orgs?per_page=100&page=" + i, Connector.REQUEST_METHOD.GET).getData();
                for (Object o1 : arr) {
                    orgs.add(new GithubOrganization(c, (JSONObject) o1));
                }
                System.out.println(arr.toString());
                return orgs;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public Mono<GithubRepository> createRepository(final Consumer<? super CreateRepositorySpec> spec) {
        return Mono.of(() -> {
            CreateRepositorySpec mutatedSpec = new CreateRepositorySpec(GithubEndpoints.user + "/repos");
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

    public Mono<Integer> unblockUser(String username) {
        return Mono.of(() -> {
            try {
                return c.readJson(GithubEndpoints.user + "/blocks/" + username, Connector.REQUEST_METHOD.DELETE, GithubApiPreviews.USER_BLOCKING).getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        });
    }

    public Mono<Boolean> isUserBlocked(String username) {
        return Mono.of(() -> {
            try {
                return c.readJson(GithubEndpoints.user + "/blocks/" + username, Connector.REQUEST_METHOD.GET, GithubApiPreviews.USER_BLOCKING).getResponseCode() == 204;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public Mono<Integer> blockUser(String username) {
        return Mono.of(() -> {
            try {
                return c.readJson(GithubEndpoints.user + "/blocks/" + username, Connector.REQUEST_METHOD.PUT, GithubApiPreviews.USER_BLOCKING).getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        });
    }

    public Mono<List<GithubUser>> getBlockedUsers() {
        return Mono.of(() -> {
            List<GithubUser> users = new ArrayList<GithubUser>();
            try {
                for(Object o : (JSONArray)c.readJson(GithubEndpoints.user + "/blocks", GithubApiPreviews.USER_BLOCKING).getData()) {
                    users.add(new GithubUser(c, (JSONObject)o));
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
            return users;
        });
    }

    public Paginator<List<String>> getEmails() {
        return Paginator.of((i) -> {
            try {
                List<String> emails = new ArrayList<>();
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.base + "/user/emails?per_page=100&page=" + i, Connector.REQUEST_METHOD.GET).getData();
                for (Object o : arr) {
                    emails.add((String) o);
                }
                return emails;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public Mono<Integer> addEmails(Collection<String> emails) {
        return Mono.of(() -> {
            try {
                JSONObject req = new JSONObject();
                JSONArray arr = new JSONArray();
                emails.forEach(e -> {
                    arr.add(e);
                });
                req.put("emails", arr);
                return c.readJson(GithubEndpoints.base + "/user/emails", Connector.REQUEST_METHOD.POST, req.toString()).getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        });
    }

    public Mono<Integer> addEmail(String email) {
        return Mono.of(() -> {
            try {
                JSONObject req = new JSONObject();
                JSONArray arr = new JSONArray();
                arr.add(email);
                req.put("emails", arr);
                return c.readJson(GithubEndpoints.base + "/user/emails", Connector.REQUEST_METHOD.POST, req.toString()).getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        });
    }

    public Mono<Integer> removeEmails(Collection<String> emails) {
        return Mono.of(() -> {
            try {
                JSONObject req = new JSONObject();
                JSONArray arr = new JSONArray();
                emails.forEach(e -> {
                    arr.add(e);
                });
                req.put("emails", arr);
                return c.readJson(GithubEndpoints.base + "/user/emails", Connector.REQUEST_METHOD.DELETE, req.toString()).getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        });
    }

    public Mono<Integer> removeEmail(String email) {
        return Mono.of(() -> {
            try {
                JSONObject req = new JSONObject();
                JSONArray arr = new JSONArray();
                arr.add(email);
                req.put("emails", arr);
                return c.readJson(GithubEndpoints.base + "/user/emails", Connector.REQUEST_METHOD.DELETE, req.toString()).getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        });
    }

    public Paginator<List<String>> getPublicEmails() {
        return Paginator.of((i) -> {
            try {
                List<String> emails = new ArrayList<>();
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.base + "/user/public_emails?per_page=100&page=" + i, Connector.REQUEST_METHOD.GET).getData();
                for (Object o : arr) {
                    emails.add((String) o);
                }
                return emails;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public Mono<Integer> follow(String username) {
        return Mono.of(() -> {
            try {
                return c.readJson(GithubEndpoints.base + "/user/following/" + username, Connector.REQUEST_METHOD.PUT).getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        });
    }

    public Mono<Integer> unfollow(String username) {
        return Mono.of(() -> {
            try {
                return c.readJson(GithubEndpoints.base + "/user/following/" + username, Connector.REQUEST_METHOD.DELETE).getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        });
    }

    public long getPublicRepositoriesCount() {
        return this.publicReposCount;
    }
    public long getPublicGistsCount() {
        return this.publicGistsCount;
    }
    public long getFollowersCount() {
        return this.followersCount;
    }
    public long getFollowingCount() {
        return this.followingCount;
    }
}
