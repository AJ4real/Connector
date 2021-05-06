package me.aj4real.connector.github.objects;

import me.aj4real.connector.Task;
import me.aj4real.connector.Response;
import me.aj4real.connector.github.GithubApiPreviews;
import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.GithubEndpoints;
import me.aj4real.connector.paginators.Paginator;
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
        this.followingCount = (long) data.get("github/following");
        this.followersCount = (long) data.get("followers");
        this.publicGistsCount = (long) data.get("public_gists");
        this.publicReposCount = (long) data.get("public_repos");
    }

    public Paginator<List<GithubOrganization>> getOrganizations() {
        return Paginator.of((i) -> {
            try {
                List<GithubOrganization> orgs = new ArrayList<GithubOrganization>();
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.AUTHENTICATED_USER_ORGANIZATIONS.addQuery("?per_page=100&page=" + i)).getData();
                for (Object o1 : arr) {
                    orgs.add(new GithubOrganization(c, (JSONObject) o1));
                }
                return orgs;
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
            }
            return null;
        });
    }

    public Task<GithubRepository> createRepository(final Consumer<? super CreateRepositorySpec> spec) {
        return Task.of(() -> {
            CreateRepositorySpec mutatedSpec = new CreateRepositorySpec(GithubEndpoints.CREATE_REPOSITORY.fulfil("owner", getLoginName()));
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

    public Task<Integer> unblockUser(String username) {
        return Task.of(() -> {
            try {
                return c.readJson(GithubEndpoints.UNBLOCK_USER.fulfil("user", username), GithubApiPreviews.USER_BLOCKING).getResponseCode();
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return -1;
            }
        });
    }

    public Task<Boolean> isUserBlocked(String username) {
        return Task.of(() -> {
            try {
                return c.readJson(GithubEndpoints.BLOCKED_USERS.fulfil("user", username), GithubApiPreviews.USER_BLOCKING).getResponseCode() == 204;
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return false;
            }
        });
    }

    public Task<Integer> blockUser(String username) {
        return Task.of(() -> {
            try {
                return c.readJson(GithubEndpoints.BLOCK_USER.fulfil("user", username), GithubApiPreviews.USER_BLOCKING).getResponseCode();
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return -1;
            }
        });
    }

    public Task<List<GithubUser>> getBlockedUsers() {
        return Task.of(() -> {
            List<GithubUser> users = new ArrayList<GithubUser>();
            try {
                for(Object o : (JSONArray)c.readJson(GithubEndpoints.LIST_BLOCKED_USERS, GithubApiPreviews.USER_BLOCKING).getData()) {
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
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.LIST_USER_EMAILS.addQuery("?per_page=100&page=" + i)).getData();
                for (Object o : arr) {
                    emails.add((String) o);
                }
                return emails;
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return null;
            }
        });
    }

    public Task<Integer> addEmails(Collection<String> emails) {
        return Task.of(() -> {
            try {
                JSONObject req = new JSONObject();
                JSONArray arr = new JSONArray();
                emails.forEach(e -> {
                    arr.add(e);
                });
                req.put("emails", arr);
                return c.readJson(GithubEndpoints.ADD_USER_EMAIL, req.toString()).getResponseCode();
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return -1;
            }
        });
    }

    public Task<Integer> addEmail(String email) {
        return Task.of(() -> {
            try {
                JSONObject req = new JSONObject();
                JSONArray arr = new JSONArray();
                arr.add(email);
                req.put("emails", arr);
                return c.readJson(GithubEndpoints.ADD_USER_EMAIL, req.toString()).getResponseCode();
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return -1;
            }
        });
    }

    public Task<Integer> removeEmails(Collection<String> emails) {
        return Task.of(() -> {
            try {
                JSONObject req = new JSONObject();
                JSONArray arr = new JSONArray();
                emails.forEach(e -> {
                    arr.add(e);
                });
                req.put("emails", arr);
                return c.readJson(GithubEndpoints.REMOVE_USER_EMAIL, req.toString()).getResponseCode();
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return -1;
            }
        });
    }

    public Task<Integer> removeEmail(String email) {
        return Task.of(() -> {
            try {
                JSONObject req = new JSONObject();
                JSONArray arr = new JSONArray();
                arr.add(email);
                req.put("emails", arr);
                return c.readJson(GithubEndpoints.REMOVE_USER_EMAIL, req.toString()).getResponseCode();
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return -1;
            }
        });
    }

    public Paginator<List<String>> getPublicEmails() {
        return Paginator.of((i) -> {
            try {
                List<String> emails = new ArrayList<>();
                JSONArray arr = (JSONArray) c.readJson(GithubEndpoints.LIST_USER_PUBLIC_EMAILS.addQuery("?per_page=100&page=" + i)).getData();
                for (Object o : arr) {
                    emails.add((String) o);
                }
                return emails;
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return null;
            }
        });
    }

    public Task<Integer> follow(String username) {
        return Task.of(() -> {
            try {
                return c.readJson(GithubEndpoints.AUTHENTICATED_USER_FOLLOW.fulfil("user", username)).getResponseCode();
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
                return -1;
            }
        });
    }

    public Task<Integer> unfollow(String username) {
        return Task.of(() -> {
            try {
                return c.readJson(GithubEndpoints.AUTHENTICATED_USER_UNFOLLOW.fulfil("user", username)).getResponseCode();
            } catch (IOException e) {
                me.aj4real.connector.Logger.handle(e);
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
