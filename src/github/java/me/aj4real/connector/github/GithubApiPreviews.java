package me.aj4real.connector.github;

public enum GithubApiPreviews {

    MIGRATIONS("wyandotte-preview"),
    ENHANCED_DEPLOYMENTS("ant-man-preview"),
    REACTIONS("squirrel-girl-preview"),
    TIMELINE("mockingbird-preview"),
    PROJECTS("inertia-preview"),
    COMMIT_SEARCH("cloak-preview"),
    USER_BLOCKING("giant-sentry-fist-preview"),
    REPOSITORY_TOPICS("mercy-preview"),
    CODES_OF_CONDUCT("scarlet-witch-preview"),
    REQUIRE_SIGNED_COMMITS("zzzax-preview"),
    REQUIRE_MULTIPLE_APPROVING_REVIEWS("luke-cage-preview"),
    POJECT_CARD_DETAILS("starfox-preview"),
    GITHUB_APP_MANIFEST("fury-preview"),
    DEPLOYMENT_STATUSES("flash-preview"),
    REPOSITORY_CREATION_PERMISSIONS("surtur-preview"),
    CONTENT_ATTACHMENTS("corsair-preview"),
    ENABLE_DISABLE_PAGES("switcheroo-preview"),
    LIST_BRANCHES_FOR_COMMIT("groot-preview"),
    LIST_PULL_REQUESTS_FOR_COMMIT("groot-preview"),
    ENABLE_DISABLE_VULNERABILITY_ALERTS("dorian-preview"),
    UPDATE_PULL_REQUESTS_BRANCH("lydian-preview"),
    ENABLE_DISABLE_AUTOMATIC_SECURITY_FIXES("london-preview"),
    CREATE_USE_REPOSITORY_TEMPLATES("baptiste-preview"),
    VISIBILITY_PARAMETER_FOR_REPOSITORIES_API("nebula-preview");

    private final String key;

    GithubApiPreviews(String key) {
        this.key = key;
    }
    public String getKey() {
        return this.key;
    }
}
